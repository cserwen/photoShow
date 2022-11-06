package com.cserwen.photo;

import com.cserwen.photo.handle.PictureHandler;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cserwen.photo.store.PictureStore;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {

    private static Javalin app;
    public static PictureStore pictureStore;
    public static PictureHandler pictureHandle;

    public static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        initApp();
        startApp();
        Runtime.getRuntime().addShutdownHook(new Thread(App::shutdown));
    }

    private static void initApp() {
        pictureStore = new PictureStore();
        pictureHandle = new PictureHandler(pictureStore);
        app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json;charset=utf-8";
            config.staticFiles.add("/static", Location.CLASSPATH);
        });
        registerHandlers();
    }

    private static void startApp() {
        boolean result;
        result = pictureStore.start();
        if (!result) {
            log.error("Failed to init picture com.cserwen.photo.store, so exit!");
            System.exit(-1);
        }
        app.start(13319);
        scheduledExecutorService.scheduleAtFixedRate(() -> pictureStore.flush(), 10 * 1000, 3 * 1000, TimeUnit.MILLISECONDS);
    }

    private static void registerHandlers() {
        app.post("/pic/upload", pictureHandle.uploadPicture);
        app.get("/pic/list/{size}/{page}", pictureHandle.getPicListByPage);
        app.get("/pic/preview/{name}", pictureHandle.getPrePicture);
        app.get("/pic/src/{name}", pictureHandle.getPicture);
    }

    private static void shutdown() {
        app.stop();
        pictureStore.shutdown();
    }
}
