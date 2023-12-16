package com.horacerta.api.controllers;

import com.horacerta.api.entities.User;
import com.horacerta.api.entities.response.ErrorResponse;
import com.horacerta.api.entities.response.SuccessResponse;
import com.horacerta.api.entities.work.CheckInAtWork;
import com.horacerta.api.entities.work.DailyWorkInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/work")
public class WorkController {

//    @PostMapping("/checkIn")
//    @Operation(summary = "Create a new work day", method = "POST")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Work day created.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))),
//            @ApiResponse(responseCode = "400", description = "Bad request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
//            @ApiResponse(responseCode = "500", description = "Server internal error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
//    })
//    public ResponseEntity<Object> checkInAtWork (@RequestParam int userId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startedAt, @RequestParam  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date finishedAt, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date lunchStartedAt, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date lunchFinishedAt) {
//
//        System.out.println("Should create user received through parameters: ");
//
//        DailyWorkInfo dailyWorkInfo = new DailyWorkInfo();
//        dailyWorkInfo.setStartedAt(startedAt);
//        dailyWorkInfo.setFinishedAt(finishedAt);
//        dailyWorkInfo.setLunchStartedAt(lunchStartedAt);
//        dailyWorkInfo.setLunchFinishedAt(lunchFinishedAt);
//        dailyWorkInfo.calculateWorkHoursForToday();
//        return null;
//    }
    @PostMapping("/checkIn")
    @Operation(summary = "Create a new work day", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Work day created.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server internal error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> checkInAtWork (@Valid @RequestBody CheckInAtWork checkInAtWork) {

        System.out.println("Should create user received through parameters: ");

        DailyWorkInfo dailyWorkInfo = new DailyWorkInfo();
        dailyWorkInfo.setStartedAt(checkInAtWork.getStartedAt());
        dailyWorkInfo.setFinishedAt(checkInAtWork.getFinishedAt());
        dailyWorkInfo.setLunchStartedAt(checkInAtWork.getLunchStartedAt());
        dailyWorkInfo.setLunchFinishedAt(checkInAtWork.getLunchFinishedAt());
        dailyWorkInfo.calculateWorkHoursForToday();
        return null;
    }

}
