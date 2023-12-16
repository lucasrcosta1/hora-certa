package com.horacerta.api.controllers;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/work")
public class WorkController implements Controller {


    //    Request Body example:
    //{
    //    "userId": 0,
    //        "startedAt": "2023-12-16T08:00:00Z",
    //        "finishedAt": "2023-12-16T16:20:00Z",
    //        "lunchStartedAt": "2023-12-16T12:00:00Z",
    //        "lunchFinishedAt": "2023-12-16T12:20:00Z"
    //}
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
        long resultHour = dailyWorkInfo.calculateExceedingHoursForToday(6);
        System.out.println("Exceeding hours: "+resultHour);
        long resultMinutes = dailyWorkInfo.calculateExceedingMinutesForToday(6, (int)resultHour);
        System.out.println("Exceeding minutes: "+resultMinutes);
        return null;
    }

}
