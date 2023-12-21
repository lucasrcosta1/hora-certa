package com.horacerta.api.entities.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone")
    private String phone;

    @Column(name = "area_of_expertise")
    private String areaOfExpertise;

    @Column(name = "hours_worked_daily")
    private Integer hoursWorkedDaily;

    @Column(name = "hours_worked_weekly")
    private Integer hoursWorkedWeekly;

    @Column(name = "created_at")
    private Date createdAt;

    public User (){}

    public User (CreateUser createUser){

        this.firstName = createUser.firstName;
        this.lastName = createUser.lastName;
        this.email = createUser.email;
        this.password = createUser.password;
        this.phone = createUser.phone;
        this.createdAt = new Date();
        this.areaOfExpertise = null;
        this.hoursWorkedDaily = null;
        this.hoursWorkedWeekly = null;

    }

    public User (User user){

        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.email = user.email;
        this.password = user.password;
        this.phone = user.phone;
        this.areaOfExpertise = user.areaOfExpertise;
        this.hoursWorkedDaily = user.hoursWorkedDaily;
        this.hoursWorkedWeekly = user.hoursWorkedWeekly;
        this.createdAt = new Date();

    }

}
