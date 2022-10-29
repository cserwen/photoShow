package utils;

import config.AppConfig;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PicUtil {

    private static final int ratio = 8;

    public static void buildPreviewPic(BufferedImage image, String target) throws IOException {
        addWaterMark(image, target, AppConfig.getPreviewWidth(), AppConfig.getPreviewHeight());
    }

    public static void addWaterMark(BufferedImage image, String target, int width, int height) throws IOException {
        BufferedImage watermark = Thumbnails.of(ImageIO.read(new File(PathUtil.getWaterMarkPic())))
                .size(width / ratio,height / ratio).asBufferedImage();
        Thumbnails.of(image).size(width, height)
                .watermark(Positions.BOTTOM_RIGHT, watermark, 1f)
                .toFile(new File(target));
    }
}
