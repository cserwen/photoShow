package utils;

import config.AppConfig;

import java.io.File;

public class PathUtil {

    public static String getPicturePath() {
        return AppConfig.getStoreRootPath() + File.separator + "pic";
    }

    public static String getConfigDir() {
        return AppConfig.getStoreRootPath() + File.separator + "cfg";
    }

    public static String getPictureInfoPath() {
        return getPicturePath() + File.separator + ".list.json";
    }

    public static String getPicturePreviewDirPath() {
        return getPicturePath() + File.separator + "preview";
    }

    public static String getPictureDirPath() {
        return getPicturePath() + File.separator + "src";
    }

    public static String getWaterMarkPic() {
        return getConfigDir() + "/watermark.png";
    }

}
