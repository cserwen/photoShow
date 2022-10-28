package handle;

import io.javalin.http.Handler;
import io.javalin.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import store.PictureItem;
import store.PictureStore;
import utils.PathUtil;
import utils.PicUtil;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public class PictureHandle {

    private final Logger log = LoggerFactory.getLogger(PictureHandle.class);
    private PictureStore pictureStore;

    public PictureHandle(PictureStore pictureStore) {
        this.pictureStore = pictureStore;
    }

    public Handler uploadPicture = ctx -> {
        String desc = ctx.formParam("desc");
        PictureItem item = new PictureItem();
        String id = String.valueOf(this.pictureStore.addPicture(item));
        AtomicInteger counter = new AtomicInteger(0);
        ctx.uploadedFiles("pics").forEach(pic -> {
            String fileName = id + "-" + counter.getAndIncrement() + pic.extension();
            String picSavePath = PathUtil.getPictureDirPath() + File.separator + fileName;
            String previewPicPath = PathUtil.getPicturePreviewDirPath() + File.separator + fileName;
            item.getNames().add(fileName);
            item.setDesc(desc);
            // save pic
            FileUtil.streamToFile(pic.content(), picSavePath);
            // build preview
            try {
                PicUtil.buildPreviewPic(picSavePath, previewPicPath);
            } catch (Exception e) {
                log.error("Failed to build preview pic for {}", picSavePath, e);
            }

        });
        log.info("success to upload files={}, desc={}", item.getNames(), desc);
    };

    public static Handler getPicListByYear = ctx -> {

    };

    public static Handler getPictureByPath = ctx -> {

    };
}
