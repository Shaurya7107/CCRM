package edu.ccrm.config;

public class AppConfig {
    private static AppConfig instance;
    private String dataFolderPath;

    private AppConfig() {
        dataFolderPath = "data/";
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public String getDataFolderPath() {
        return dataFolderPath;
    }
}