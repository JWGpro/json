package com.nps.json;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming
public class Person {
    // TODO combine String var declarations?
    // TODO var name conversion strategy
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
}
