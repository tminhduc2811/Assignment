package com.ducta;

import com.ducta.csvhandler.CSVParser;
import com.ducta.drawer.Drawer;
import com.ducta.h2handler.H2Handler;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) throws IOException {

        TreeMap<String, TreeMap<String, Integer>> casesByStateAndDate = null;

        Scanner in = new Scanner(System.in);

        // Get DB Path
        System.out.println("Please provide the DB path:");
        String dbPath = in.nextLine();

        // Get Input Path
        System.out.println("And also the input folder: ");
        String inputFolder = in.nextLine();

        // Get Output Path
        System.out.println("Finally, the output folder: ");
        String outputFolder = in.nextLine();

        boolean exit = false;

        File folder = new File(inputFolder);
        File[] listFiles = folder.listFiles();

        CSVParser csvParser;
        printOptions();
        while (!exit) {
            String option = in.nextLine();

            switch (option) {
                case "1":
                    csvParser = new CSVParser(listFiles);
                    casesByStateAndDate = csvParser.parseFromCSV();
                    System.out.println("Done. Choose next option.");
                    printOptions();
                    break;
                case "2":
                    if (casesByStateAndDate != null) {
                        H2Handler h2Handler = new H2Handler(casesByStateAndDate, dbPath);
                        try {
                            h2Handler.saveToH2();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        System.out.println("Done. Choose next option.");
                    } else {
                        System.out.println("Please parse data from csv files first!");
                    }
                    printOptions();
                    break;
                case "3":
                    if (casesByStateAndDate != null) {
                        Drawer.drawFromRawData(casesByStateAndDate, outputFolder);
                        System.out.println("Done. Choose next option.");
                    } else {
                        System.out.println("Please parse data from csv files first!");
                    }
                    printOptions();
                    break;
                case "4":
                    if (casesByStateAndDate != null) {
                        Drawer.drawFromRawDataWithMultiThread(casesByStateAndDate, outputFolder);
                        System.out.println("Done. Choose next option.");
                    } else {
                        System.out.println("Please parse data from csv files first!");
                    }
                    printOptions();
                    break;
                case "5":
                    exit = true;
                    break;
            }
        }
    }

    static void printOptions() {

        System.out.println("Choose your options:");
        System.out.println("1. Parse data from CSV files.");
        System.out.println("2. Save parsed data to H2 DB");
        System.out.println("3. Create charts from raw data.");
        System.out.println("4. Create charts from raw data with MultiThread");
        System.out.println("5. Exit");

    }
}
