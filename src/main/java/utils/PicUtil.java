package utils;

import config.AppConfig;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class PicUtil {

    public static void buildPreviewPic(String source, String target) throws IOException {
        Thumbnails.of(new File(source))
            .size(AppConfig.getPreviewWidth(), AppConfig.getPreviewHeight())
            .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(PathUtil.getWaterMarkPic())), 1f)
            .toFile(new File(target));
    }
}
