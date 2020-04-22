package com.nps.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        // Method 2 (filter first, combine sort, output stream): ~400 ms
        Checkpoint.mark("start");

        // Parse JSON: ~250 ms
        Person[] ppl = new ObjectMapper().readValue(new File("./person test data.json"), Person[].class);

        System.out.println(String.format("Raw length: %d", ppl.length));
        Checkpoint.mark("JSON parsed");

        // Filter by DOB: ~50 ms
        List<Person> people = new ArrayList<>(Arrays.asList(ppl));
        people.removeIf(Person::bornAfter2000);  // how is this slower than sorting?

        System.out.println(String.format("Filtered by DOB: %d", people.size()));
        Checkpoint.mark("DOB-filtering");

        // Remove duplicates; sort: ~5 ms
        Set<Person> uniques = new TreeSet<>((o1, o2) -> {
            if (
                    o1.first_name.equalsIgnoreCase(o2.first_name) &&
                    o1.middle_name.equalsIgnoreCase(o2.middle_name) &&
                    o1.last_name.equalsIgnoreCase(o2.last_name) &&
                    o1.date_of_birth.equals(o2.date_of_birth)
            ) {
                // remove these: duplicates
                return 0;
            }
            // sort by first name, then last name
            //  don't allow return 0 as this would remove the object despite not being a dupe
            //  but can't do Python-like truthy evaluations (return 0 or 0 or 1)
            int result = o1.first_name.compareTo(o2.first_name);
            if (result == 0) {
                result = o1.last_name.compareTo(o2.last_name);
                if (result == 0) {
                    result = 1;
                }
            }
            return result;
        });
        uniques.addAll(people);

        System.out.println(String.format("Duplicates removed; sorted: %d", uniques.size()));
        Checkpoint.mark("duplicates removed; sorted");

        // Print: ~90 ms
        // not much faster at all for this file
        Writer out = new OutputStreamWriter(System.out);
        for (Person person : uniques) {
            out.write(person.detail());
        }
        out.flush();

        Checkpoint.mark("printed");
    }
}
