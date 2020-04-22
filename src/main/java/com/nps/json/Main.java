package com.nps.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Checkpoint.mark("start");

        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost/nps_json_temp",
                            "postgres", "password");  // user-pass
            Checkpoint.mark("DB connection");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }
}
