package com.nps.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        Checkpoint.mark("start");

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost/nps_json_temp",
                            "postgres", "password");  // user-pass
            Checkpoint.mark("DB connection");

            stmt = c.createStatement();
            String sql;

            sql = "CREATE TABLE people " +
                    "(ID SERIAL PRIMARY KEY," +
                    " first_name     TEXT    NOT NULL, " +
                    " middle_name    TEXT, " +
                    " last_name      TEXT    NOT NULL, " +
                    " date_of_birth  TEXT    NOT NULL)";
            stmt.executeUpdate(sql);
            Checkpoint.mark("table created");

            jsonStreamWrite(c);
            Checkpoint.mark("table populated");

            outputQuery(c);

            sql = "DROP TABLE people;";
            stmt.executeUpdate(sql);
            Checkpoint.mark("table dropped");

            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    private static void jsonStreamWrite(Connection c) throws IOException, SQLException {
        c.setAutoCommit(false);

        JsonParser jParser = new JsonFactory().createParser(new File("./person test data.json"));

        String firstName = null;
        String middleName = null;
        String lastName = null;
        String dateOfBirth = null;

        String sql = "INSERT INTO people (first_name, middle_name, last_name, date_of_birth) " +
                "VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = c.prepareStatement(sql);

        // JSON array?
        if (jParser.nextToken() == JsonToken.START_ARRAY) {
            while (jParser.nextToken() != JsonToken.END_ARRAY) {

                // object
                while (jParser.nextToken() != JsonToken.END_OBJECT) {
                    String field = jParser.getCurrentName();

                    if ("first_name".equals(field)) {
                        jParser.nextToken();
                        firstName = jParser.getText();
                    }
                    if ("middle_name".equals(field)) {
                        jParser.nextToken();
                        middleName = jParser.getText();
                    }
                    if ("last_name".equals(field)) {
                        jParser.nextToken();
                        lastName = jParser.getText();
                    }
                    if ("date_of_birth".equals(field)) {
                        jParser.nextToken();
                        dateOfBirth = jParser.getText();
                    }
                }
                // filter by DOB
                // only insert data we'll want back after sorting
                if (dateOfBirth != null && !bornAfter2000(dateOfBirth)) {
                    // >3000 ms on the test file if not c.setAutoCommit(false).
                    stmt.setString(1, firstName);
                    stmt.setString(2, middleName);
                    stmt.setString(3, lastName);
                    stmt.setString(4, dateOfBirth);
                    stmt.executeUpdate();
                }

            }
        }
        jParser.close();

        c.commit();
        c.setAutoCommit(true);
    }

    private static boolean bornAfter2000(String str) {
        // Can only be 1 (1999 or earlier) or 2 (2000 or later).
        return str.charAt(0) == '2';
    }

    private static void outputQuery(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM people;" );
        Checkpoint.mark("DB queried");

        while ( rs.next() ) {
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String dateOfBirth = rs.getString("date_of_birth");

            // TODO: output stream
            String output = String.format("I am %s %s, %d years old.", firstName, lastName, ageYears(dateOfBirth));
            System.out.println(output);
        }
        Checkpoint.mark("query printed");

        rs.close();
        stmt.close();
    }

    private static Integer ageYears(String dobStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate then = LocalDate.parse(dobStr, formatter);

        ZoneId zone = ZoneId.of("Z");  // Timezone: UTC
        LocalDate now = LocalDate.now(zone);

        Period period = Period.between(then, now);

        return period.getYears();
    }
}
