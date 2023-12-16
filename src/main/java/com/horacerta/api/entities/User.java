package com.horacerta.api.entities;

import lombok.Data;

import java.util.Date;

@Data
public class User {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private int phone;
    private String areaOfExpertise;
    private int hoursWorkedDaily;
    private int hoursWorkedWeekly;
    private Date createdAt;

}
