package edu.ccrm;

import edu.ccrm.cli.Menu;
import edu.ccrm.config.AppConfig;

public class Main {
    public static void main(String[] args) {
        AppConfig config = AppConfig.getInstance();
        System.out.println("CCRM Started with data folder: " + config.getDataFolderPath());

        Menu menu = new Menu();
        menu.displayMainMenu();
    }
}