package com.horacerta.api.controllers;

import com.horacerta.api.entities.response.ErrorResponse;
import com.horacerta.api.entities.response.SuccessResponse;
import com.horacerta.api.entities.work.CheckInAtWork;
import com.horacerta.api.entities.work.DailyWorkInfo;
import com.horacerta.api.repositories.UserRepository;
import com.horacerta.api.repositories.WorkRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/work")
public class WorkController {

    WorkRepository workRepository;

    @Autowired
    public WorkController(WorkRepository workRepository) {
        this.workRepository = workRepository;
    }

    @GetMapping("/all")
    @Operation(summary = "Get all work info inputted for a user", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Work information retrieved.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DailyWorkInfo[].class))),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server internal error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> getAllWorkRegistryForUser (int userId) {

        return ResponseEntity.ok(workRepository.findAllUserOccurences(userId));

    }

    //    Request Body example:
    //{
    //    "userId": 0,
    //        "startedAt": "2023-12-16T08:00:00Z",
    //        "finishedAt": "2023-12-16T16:20:00Z",
    //        "lunchStartedAt": "2023-12-16T12:00:00Z",
    //        "lunchFinishedAt": "2023-12-16T12:20:00Z"
    //}

    // TODO: 22/12/23 - Handle time issue on requests. The request '2023-12-23 08:00:00' arrives on the backend as '2023-12-23 05:00:00'
    @PostMapping("/checkIn")
    @Operation(summary = "Create a new work day", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Work day created.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server internal error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> checkInAtWork (@Valid @RequestBody CheckInAtWork checkInAtWork) {

        try {

            int registerQueryResponse = workRepository.registerDailyWork(
                    checkInAtWork.getUserId(),
                    addHoursToDate(checkInAtWork.getStartedAt(), 3),
                    addHoursToDate(checkInAtWork.getFinishedAt(), 3),
                    addHoursToDate(checkInAtWork.getLunchStartedAt(), 3),
                    addHoursToDate(checkInAtWork.getLunchFinishedAt(), 3),
                    checkInAtWork.isDayOff(),
                    checkInAtWork.isVacation(),
                    new Date(),
                    new Date()
            );
            if (registerQueryResponse > 0)
                return ResponseEntity.ok(new SuccessResponse("Work information added","User's work information addedd with success."));
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("User already checked in.", "If you'd like to change its checkin information, go to edition area."));

        } catch (DataIntegrityViolationException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("User not found","User with id: " + checkInAtWork.getUserId() + ", not found."));

        } catch (Exception e) {

            System.err.println("Checkin endpoint-> Exception not predicted - " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ErrorResponse("Internal server error.", "An error occured, please try again later."));
        }

    }

    /**
     * Add time zone difference to the date arrived.
     * @param date Date
     * @param hours int
     * @return Date
     */
    private static Date addHoursToDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

}
