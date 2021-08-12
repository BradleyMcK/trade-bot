package com.zollos.crypto.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.TimeZone;

public class ConfigSetup {

    /*
    private static final StringBuilder setup = new StringBuilder();
    private static List<String> currencies;
    private static String fiat;

    public ConfigSetup() {
        throw new IllegalStateException("Utility class");
    }

    public static String getSetup() {
        return setup.toString();
    }

    public static List<String> getCurrencies() {
        return currencies;
    }

    public static int getRequestLimit() {
        return REQUEST_LIMIT;
    }

    public static String getFiat() {
        return fiat;
    }
    */

    public static void readConfig() {

        //Formatter.getSimpleFormatter().setTimeZone(TimeZone.getDefault());
        int items = 0;
        File file = new File("config.txt");
        if (!file.exists()) {
            System.out.println("No config file detected!");
            new Scanner(System.in).nextLine();
            System.exit(1);
        }

        try (FileReader reader = new FileReader(file);
             BufferedReader br = new BufferedReader(reader)) {

            String line;
            System.out.println("Printing config file contents:");

            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
