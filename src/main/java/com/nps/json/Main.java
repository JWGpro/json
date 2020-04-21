package com.nps.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        System.out.println("Parsing JSON...\n");

        // TODO separate methods, throw JSON exceptions?
        Person[] people = new ObjectMapper().readValue(new File("./person test data.json"), Person[].class);

        people[0].detail();

        // TODO
        //  naive strategy: add to new array sortedPeople, sorting as you go. binary search thing?
        // TODO

//        // print the list
//        for (Person person : sortedPeople) {
//            person.detail();
//        }

        long timeLapsed = (System.currentTimeMillis() - startTime);
        System.out.println(String.format("\nExecution time: %d ms", timeLapsed));
    }
}
