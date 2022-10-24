package config;

import constants.ConfigKey;

import java.util.ResourceBundle;

public class AppConfig {

    private static final ResourceBundle COMMON_BUNDLE = ResourceBundle.getBundle("app");

    public static String getStoreRootPath () {
        return COMMON_BUNDLE.getString(ConfigKey.STORE_ROOT_PATH);
    }
}
