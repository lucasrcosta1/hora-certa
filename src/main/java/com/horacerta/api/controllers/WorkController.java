package com.horacerta.api.controllers;

import com.horacerta.api.entities.response.ErrorResponse;
import com.horacerta.api.entities.response.SuccessResponse;
import com.horacerta.api.entities.work.CheckInAtWork;
import com.horacerta.api.entities.work.DailyWorkInfo;
import com.horacerta.api.repositories.WorkRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;


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

    // TODO: 22/12/23 - Handle time zone issue on requests. The request '2023-12-23 08:00:00' arrives on the backend as '2023-12-23 05:00:00'
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

    // TODO: 22/12/23 - Handle time zone issue on requests. The request '2023-12-23 08:00:00' arrives on the backend as '2023-12-23 05:00:00'
    @PostMapping("/checkIn-multiple")
    @Operation(summary = "Create multiple new work days", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Work days created.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server internal error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> checkInMultipleDaysAtWork (@Valid @RequestBody CheckInAtWork[] checkInAtWorkDays) {

        int i = 0;
        for (CheckInAtWork checkInAtWork: checkInAtWorkDays) {
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
                if (registerQueryResponse > 0) {
                    if (i == checkInAtWorkDays.length - 1)
                        return ResponseEntity.ok(new SuccessResponse("Work information added", checkInAtWorkDays.length + " work information addedd with success."));
                    i++;
                } else
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("User already checked in.", "If you'd like to change its checkin information, go to edition area."));

            } catch (DataIntegrityViolationException e) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("User not found","User with id: " + checkInAtWork.getUserId() + ", not found."));

            } catch (Exception e) {

                System.err.println("Checkin endpoint-> Exception not predicted - " + e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ErrorResponse("Internal server error.", "An error occured, please try again later."));
            }
        } return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ErrorResponse("Internal server error.", "An error occured, please try again later."));

    }

    @PutMapping("/update")
    @Operation(summary = "Update a work day", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Work day updated.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "404", description = "Work day not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server internal error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> checkInAtWork (@Valid @RequestParam int dailyWorkedId, @Valid @RequestBody CheckInAtWork checkInAtWork) {

        Optional<DailyWorkInfo> optionalDailyWorkInfo = workRepository.findById(dailyWorkedId);
        if (optionalDailyWorkInfo.isPresent()) {

            DailyWorkInfo dailyWorkInfo = optionalDailyWorkInfo.get();
            if (workRepository.updateDailyWorkInfo(
                dailyWorkedId,
                checkInAtWork.getUserId(),
                addHoursToDate(checkInAtWork.getStartedAt(), 3),
                addHoursToDate(checkInAtWork.getFinishedAt(), 3),
                addHoursToDate(checkInAtWork.getLunchStartedAt(), 3),
                addHoursToDate(checkInAtWork.getLunchFinishedAt(), 3),
                checkInAtWork.isDayOff(),
                checkInAtWork.isVacation(),
                new Date()
            ) > 0) {
                return ResponseEntity.ok(new SuccessResponse("Work information updated.", "Work information updated with success."));
            } else {
                System.err.println("update endpoint-> Condition not predicted - ");
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ErrorResponse("Internal server error.", "An error occured, please try again later."));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Work day information not found.", "Work registry with id: " + dailyWorkedId + " wasn't found."));

    }

    @DeleteMapping("/remove")
    @Operation(summary = "Remove work day by given id.", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Work day removed.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "404", description = "Work day not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> removeWorkedDay (@RequestParam int workDayId) {

        if (workRepository.removeDailyWorkInfo(workDayId) > 0)
            return ResponseEntity.ok(new SuccessResponse("Work day removed.", "Work day id: " + workDayId + " removed successfuly."));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Work day not found.", "Work day with id: " + workDayId + " wasn't found."));
    }

    @DeleteMapping("/remove-multiple")
    @Operation(summary = "Remove multiple work day by given id.", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Work days removed.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "404", description = "Work days not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> removeMultipleWorkedDay (@RequestParam int[] workDayIds) {

        int numberOfSuccessfulRemoval = 0;
        for (int workDayId : workDayIds) {

            if (workRepository.removeDailyWorkInfo(workDayId) > 0) {
                if (numberOfSuccessfulRemoval == workDayIds.length - 1)
                    return ResponseEntity.ok(new SuccessResponse("Work days removed.", workDayIds.length + " work days removed successfuly."));
                numberOfSuccessfulRemoval++;

                } else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Work day not found.", "Work day with id: " + workDayId + " wasn't found."));

        }
        System.err.println("remove-multiple endpoint-> Condition not predicted - ");
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ErrorResponse("Internal server error.", "An error occured, please try again later."));

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
