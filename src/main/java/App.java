import handle.PictureHandle;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import store.PictureStore;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {

    private static Javalin app;
    public static PictureStore pictureStore;
    public static PictureHandle pictureHandle;

    public static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        initApp();
        startApp();
        Runtime.getRuntime().addShutdownHook(new Thread(App::shutdown));
    }

    private static void initApp() {
        pictureStore = new PictureStore();
        pictureHandle = new PictureHandle(pictureStore);
        app = Javalin.create(config -> {});
        registerHandlers();
    }

    private static void startApp() {
        boolean result;
        result = pictureStore.start();
        if (!result) {
            log.error("Failed to init picture store, so exit!");
            System.exit(-1);
        }
        app.start();
        scheduledExecutorService.scheduleAtFixedRate(() -> pictureStore.flush(), 10 * 1000, 3 * 1000, TimeUnit.MILLISECONDS);
    }

    private static void registerHandlers() {
        app.post("/pic/upload", pictureHandle.uploadPicture);
        app.get("/pic/list", PictureHandle.getPicListByYear);
        app.get("/pic/{year}/{mouth}/{day}/{pic}", PictureHandle.getPictureByPath);
    }

    private static void shutdown() {
        app.stop();
        pictureStore.shutdown();
    }
}
