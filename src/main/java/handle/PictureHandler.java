package handle;

import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import store.PictureItem;
import store.PictureStore;
import utils.GsonUtil;
import utils.PathUtil;
import utils.PicUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PictureHandler {

    private final Logger log = LoggerFactory.getLogger(PictureHandler.class);
    private PictureStore pictureStore;

    public PictureHandler(PictureStore pictureStore) {
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
            item.setDesc(desc == null ? "" : desc);
            try {
                BufferedImage image = ImageIO.read(pic.content());
                //build preview
                PicUtil.buildPreviewPic(image, previewPicPath);
                // save pic
                PicUtil.addWaterMark(image, picSavePath, image.getWidth(), image.getHeight());
            } catch (Exception e) {
                log.error("Failed to build preview pic for {}", picSavePath, e);
            }

        });
        log.info("success to upload files={}, desc={}", item.getNames(), desc);
    };

    public Handler getPicListByPage = ctx -> {
        long page = Long.parseLong(ctx.pathParam("page"));
        long size = Long.parseLong(ctx.pathParam("size"));
        List<PictureItem> pictureItems = pictureStore.getItemsByPage(page, size);
        ctx.result(GsonUtil.toJson(pictureItems));
    };

    public Handler getPrePicture = ctx -> {
        String name = ctx.pathParam("name");
        String filePath = PathUtil.getPicturePreviewDirPath() + File.separator + name;
        ctx.contentType("image/jpeg");
        File file = new File(filePath);
        if (file.exists()) {
            ctx.result(new FileInputStream(filePath));
        }
    };

    public Handler getPicture = ctx -> {
        String name = ctx.pathParam("name");
        String filePath = PathUtil.getPictureDirPath() + File.separator + name;
        ctx.contentType("image/jpeg");
        File file = new File(filePath);
        if (file.exists()) {
            ctx.result(new FileInputStream(filePath));
        }
    };
}
