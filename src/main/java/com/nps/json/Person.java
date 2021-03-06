package com.nps.json;

// TODO combine String var declarations?
// TODO PropertyNamingStrategy

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Person {
    public String person_id;
    public String created_datetime;
    public String created_by_username;
    public String updated_datetime;
    public String updated_by_username;
    public String first_name;
    public String middle_name;
    public String last_name;
    public String date_of_birth;
    public Boolean deleted;
    public Integer gender;
    public Integer ethnicity;
    public Integer nationality;
    public Integer preferred_language;
    public Integer religion;
    public String other;
    public Integer status_id;
    public String person_reference_number;
    public String mobile_phone;
    public String other_phone;
    public String email_address;
    public String staff_allocation;
    public String team_allocation;
    public String interpreter_required;
    public String person_custody_details_id;
    public String team_id;
    public String org_id;
    public String area_id;

    public String detail() {
        return String.format("I am %s %s, %d years old.\n", first_name, last_name, ageYears());
    }

    public LocalDate dobDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date_of_birth, formatter);
    }

    public boolean bornAfter2000() {
        // Can only be 1 (1999 or earlier) or 2 (2000 or later).
        return date_of_birth.charAt(0) == '2';
    }

    private Integer ageYears() {
        ZoneId zone = ZoneId.of("Z");  // Timezone: UTC
        LocalDate now = LocalDate.now(zone);

        Period period = Period.between(dobDate(), now);

        return period.getYears();
    }
}
