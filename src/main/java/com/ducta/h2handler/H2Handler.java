package com.ducta.h2handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class H2Handler {

    static final String DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:file:D:\\Project\\Java\\assignment\\db";

    //  Database credentials
    static final String USER = "sa";
    static final String PASS = "";

    private final TreeMap<String, TreeMap<String, Integer>> casesByStateAndDate;

    public H2Handler(TreeMap<String, TreeMap<String, Integer>> casesByStateAndDate) {
        this.casesByStateAndDate = casesByStateAndDate;
    }

    private String getDateFromFileName(String fileName) {
        return fileName.substring(0, 7);
    }

    public void saveToH2() throws SQLException {
        Connection connection = null;
        Statement stmt = null;
        try {

            Class.forName(DRIVER);
            System.out.println("Connecting to database");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

            // Create schema
            System.out.println("Creating table...");
            stmt = connection.createStatement();
            String sql = "DROP TABLE IF EXISTS `data`;\n" +
                    "CREATE TABLE `data` (\n" +
                    "\t`state` \t\tvarchar(100) \tNOT NULL,\n" +
                    "    `cases`\t\t\tint\t\t\t\tNOT NULL,\n" +
                    "    `total_cases`\tint\t\t\t\tNOT NULL,\n" +
                    "    `date`\t\t\tvarchar(100)\tNOT NULL\n" +
                    ");";
            stmt.executeUpdate(sql);

            System.out.println("Table created");
            System.out.println("Insert to table");

            System.out.println("Saving to database...");
            for (Map.Entry<String, TreeMap<String, Integer>> entry : this.casesByStateAndDate.entrySet()) {
                String state = entry.getKey();
                Integer total = 0;
                for (Map.Entry<String, Integer> entry1 : this.casesByStateAndDate.get(state).entrySet()) {
                    String date = entry1.getKey();
                    total += entry1.getValue();
                    stmt.executeUpdate(buildQuery(state, date, entry1.getValue(), total));
                }
            }
            System.out.println("Database saved.");
            stmt.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    private String buildQuery(String state, String date, Integer cases, Integer totalCases) {
        StringBuilder query = new StringBuilder("INSERT INTO DATA ");
        query.append("VALUES (").append("'").append(state).append("'").append(", ").append(cases).append(", ").append(totalCases)
                .append(", ").append("'").append(date).append("'").append(")");
        return query.toString();
    }

}
