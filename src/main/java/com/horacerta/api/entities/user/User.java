package com.horacerta.api.entities.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC+3")
    @Schema(type = "string", pattern = "yyyy-MM-dd HH:mm:ss", example = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC+3")
    @Schema(type = "string", pattern = "yyyy-MM-dd HH:mm:ss", example = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    public User (){}

    public User (CreateUser createUser){

        this.firstName = createUser.firstName;
        this.lastName = createUser.lastName;
        this.email = createUser.email;
        this.password = createUser.password;
        this.phone = createUser.phone;
        this.createdAt = new Date();
        this.updatedAt = new Date();
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
        this.updatedAt = new Date();

    }

    public void updateUserInfo(UpdateUser request) {

        boolean changed = false;
        if (!request.getFirstName().equals(this.getFirstName())) {
            this.setFirstName(request.getFirstName());
            changed = true;
        }
        if (!request.getLastName().equals(this.getLastName())) {
            this.setLastName(request.getLastName());
            changed = true;
        }
        if (!request.getEmail().equals(this.getEmail())) {
            this.setEmail(request.getEmail());
            changed = true;
        }
        if (!request.getPhone().equals(this.getPhone())) {
            this.setPhone(request.getPhone());
            changed = true;
        }
        if (!request.getAreaOfExpertise().equals(this.getAreaOfExpertise())) {
            this.setAreaOfExpertise(request.getAreaOfExpertise());
            changed = true;
        }
        if (request.getHoursWorkedDaily() != this.getHoursWorkedDaily()) {
            this.setHoursWorkedDaily(request.getHoursWorkedDaily());
            changed = true;
        }
        if (request.getHoursWorkedWeekly() != this.getHoursWorkedWeekly()) {
            this.setHoursWorkedWeekly(request.getHoursWorkedWeekly());
            changed = true;
        }
        if (changed) this.setUpdatedAt(new Date());
        
    }

}
