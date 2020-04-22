package com.nps.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Checkpoint.mark("start");

        System.out.println("Parsing JSON...");
        Person[] people = new ObjectMapper().readValue(new File("./person test data.json"), Person[].class);
        Checkpoint.mark("JSON parsed");

        // Method 1

        // Remove duplicates
        System.out.println(String.format("Raw length: %d", people.length));
        Set<Person> uniques = new TreeSet<>(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                if (o1.first_name.equalsIgnoreCase(o2.first_name) &&
                        o1.middle_name.equalsIgnoreCase(o2.middle_name) &&
                        o1.last_name.equalsIgnoreCase(o2.last_name) &&
                        o1.date_of_birth.equals(o2.date_of_birth)
                ) {
                    return 0;
                }
                return 1;
            }
        });
        uniques.addAll(Arrays.asList(people));
        // TODO: is this already sorted? then need to specify first.
        System.out.println(String.format("Duplicates removed: %d", uniques.size()));
        Checkpoint.mark("duplicates removed");

        // Filter by DOB
        uniques.removeIf(person -> person.dobDate().isAfter(LocalDate.parse("1999-12-31")));
        System.out.println(String.format("Filtered by DOB: %d", uniques.size()));
        Checkpoint.mark("DOB-filtering");

        // Sort

        // Print

        // TODO
        //  naive strategy: add to new array sortedPeople, sorting as you go. binary search thing?
        // TODO

//        // print the list
//        for (Person person : sortedPeople) {
//            person.detail();
//        }
    }
}
