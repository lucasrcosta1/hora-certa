package com.horacerta.api.repositories;

import com.horacerta.api.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail (String email);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO users (first_name, last_name, email, password, phone, created_at) " +
            "VALUES (:firstName, :lastName, :email, :password, :phone, :createdAt) " +
            "ON CONFLICT (email) DO NOTHING",
            nativeQuery = true)
    int registerNewUser(
       @Param("firstName") String firstName,
       @Param("lastName") String lastName,
       @Param("email") String email,
       @Param("password") String password,
       @Param("phone") String phone,
       @Param("createdAt") Date createdAt
    );

    @Transactional
    @Modifying
    @Query(value = "UPDATE User u SET u.areaOfExpertise = :areaOfExpertise, u.hoursWorkedDaily = :hoursWorkedDaily, u.hoursWorkedWeekly = :hoursWorkedWeekly WHERE u.id = :id")
    int registerUserWorkInfo(
            @Param("id") int id,
            @Param("areaOfExpertise") String areaOfExpertise,
            @Param("hoursWorkedDaily") int hoursWorkedDaily,
            @Param("hoursWorkedWeekly") int hoursWorkedWeekly
    );

    List<User> findAll();
}
