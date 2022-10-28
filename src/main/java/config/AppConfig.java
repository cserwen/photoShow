package config;

import constants.ConfigKey;

import java.util.ResourceBundle;

public class AppConfig {

    private static final ResourceBundle COMMON_BUNDLE = ResourceBundle.getBundle("app");

    public static String getStoreRootPath() {
        return COMMON_BUNDLE.getString(ConfigKey.STORE_ROOT_PATH);
    }

    public static int getPreviewWidth() {
        if (COMMON_BUNDLE.containsKey(ConfigKey.PIC_PREVIEW_WIDTH)) {
            return Integer.parseInt(COMMON_BUNDLE.getString(ConfigKey.PIC_PREVIEW_WIDTH));
        }
        return 800;
    }

    public static int getPreviewHeight() {
        if (COMMON_BUNDLE.containsKey(ConfigKey.PIC_PREVIEW_HEIGHT)) {
            return Integer.parseInt(COMMON_BUNDLE.getString(ConfigKey.PIC_PREVIEW_HEIGHT));
        }
        return 600;
    }
}
