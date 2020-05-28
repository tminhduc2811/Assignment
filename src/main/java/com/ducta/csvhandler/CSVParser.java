package com.ducta.csvhandler;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.util.*;

public class CSVParser {

    private static final String CSV_EXTENSION = ".csv";
    private static final String PREFIX = "cdc-state-case-counts-";

    private final TreeMap<String, TreeMap<String, Integer>> casesByStateAndDate = new TreeMap<>();

    private final File[] listFiles;

    public CSVParser(File[] listFiles) {
        this.listFiles = listFiles;
    }

    public TreeMap<String, TreeMap<String, Integer>> parseFromCSV() {
        try {
            for (File file : listFiles) {

                String filePath = file.getAbsolutePath();

                String fileName = getFileName(filePath);

                String fileDate = getDateFromFileName(fileName);

                FileReader fileReader = new FileReader(filePath);

                CSVReader csvReader = new CSVReader(fileReader);

                String[] nextRecord;

                // Skip header
                nextRecord = csvReader.readNext();

                while ((nextRecord = csvReader.readNext()) != null) {
                    TreeMap<String, Integer> casesByDate = this.casesByStateAndDate.get(nextRecord[0]);

                    Integer cases;
                    if (casesByDate != null) {

                        cases = casesByDate.get(fileDate);

                        if (cases != null) {
                            cases += Integer.parseInt(nextRecord[1]) + Integer.parseInt(nextRecord[2]);
                        } else {
                            cases = Integer.parseInt(nextRecord[1]) + Integer.parseInt(nextRecord[2]);
                        }
                    } else {
                        casesByDate = new TreeMap<>();
                        cases = Integer.parseInt(nextRecord[1]) + Integer.parseInt(nextRecord[2]);
                    }
                    casesByDate.put(fileDate, cases);
                    this.casesByStateAndDate.put(nextRecord[0], casesByDate);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this.casesByStateAndDate;
    }

    private String getFileName(String filePath) {
        return filePath.split(PREFIX)[1].split(CSV_EXTENSION)[0];
    }

    private String getDateFromFileName(String fileName) {
        return fileName.substring(0, 7);
    }

}
