package com.ducta;

import com.ducta.csvhandler.CSVParser;
import com.ducta.drawer.Drawer;
import com.ducta.h2handler.H2Handler;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

public class Main
{
    public static void main( String[] args ) throws IOException {

        TreeMap<String, TreeMap<String, Integer>> casesByStateAndDate;

        String dbPath = "D:\\Project\\Java\\assignment\\db";

        // Get all files
        File folder = new File("D:\\Project\\Java\\assignment\\input");
        File[] listFiles = folder.listFiles();

        CSVParser csvParser = new CSVParser(listFiles, dbPath);

        casesByStateAndDate = csvParser.parseFromCSV();

        // Save to DB
        H2Handler h2Handler = new H2Handler(casesByStateAndDate);
        try {
            h2Handler.saveToH2();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Drawer.drawFromRawData(casesByStateAndDate);
    }

}
