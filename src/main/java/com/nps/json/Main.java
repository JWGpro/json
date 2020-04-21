package com.nps.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Parsing JSON...");

        // TODO separate methods, throw JSON exceptions?
        Person[] people = new ObjectMapper().readValue(new File("./person test data.json"), Person[].class);

        System.out.println(people[0].person_id);
    }
}
