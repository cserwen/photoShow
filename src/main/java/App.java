import handle.PictureHandle;
import io.javalin.Javalin;
import utils.PathUtil;

public class App {

    private static Javalin app;

    public static void main(String[] args) {
        initApp();
        startApp();
        Runtime.getRuntime().addShutdownHook(new Thread(App::shutdown));
    }

    private static void initApp() {
        app = Javalin.create(config -> {
        });
        registerHandlers();
    }

    private static void startApp() {
        app.start();
    }

    private static void registerHandlers() {
        app.post("/pic/upload", PictureHandle.uploadPicture);
        app.get("/pic/list", PictureHandle.getPicListByYear);
        app.get("/pic/{year}/{mouth}/{day}/{pic}", PictureHandle.getPictureByPath);
    }

    private static void shutdown() {
        app.stop();
    }
}
