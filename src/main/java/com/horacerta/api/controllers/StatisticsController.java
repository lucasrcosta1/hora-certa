package com.horacerta.api.controllers;

import com.horacerta.api.entities.response.ErrorResponse;
import com.horacerta.api.entities.statistics.ChosenRange;
import com.horacerta.api.entities.statistics.StatisticsResponse;
import com.horacerta.api.entities.work.DailyWorkInfo;
import com.horacerta.api.repositories.StatisticsRepository;
import com.horacerta.api.repositories.UserRepository;
import com.horacerta.api.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    UserRepository userRepository;
    StatisticsRepository statisticsRepository;
    StatisticsService statisticsService;

    @Autowired
    public StatisticsController (UserRepository userRepository, StatisticsRepository statisticsRepository, StatisticsService statisticsService) {

        this.userRepository = userRepository;
        this.statisticsRepository = statisticsRepository;
        this.statisticsService = statisticsService;

    }

    @GetMapping("/day")
    @Operation(summary = "Get today's work information", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Work information retrieved.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StatisticsResponse.class))),
            @ApiResponse(responseCode = "404", description = "Work information not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<Object> day (@RequestParam("userId") int userId) {

        DailyWorkInfo workDay = this.statisticsRepository.findUserWorkByGivenDay(userId, new Date());
        if (workDay != null) {
            StatisticsResponse statisticsResponse = this.statisticsService.getDailyStatistics(workDay);
            if (statisticsResponse != null) {
                return ResponseEntity.ok(statisticsResponse);
            }
            System.err.println("day endpoint-> Condition not predicted - ");
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ErrorResponse("Internal server error.", "An error occurred, please try again later."));
        } return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Work information not found.", "There is no work registry today for the user with id: "+ userId+"."));
    }

    @GetMapping("/week")
    @Operation(summary = "Get this week's work information", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Work information retrieved.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StatisticsResponse.class))),
            @ApiResponse(responseCode = "404", description = "Work information not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<Object> week (@RequestParam("userId") int userId) {

        Date start = this.statisticsService.subtractDays(new Date(), 7), end = new Date();
        Set<DailyWorkInfo> workDays = this.statisticsRepository.findUserWorkByGivenInterval(userId, start, end);
        if (!workDays.isEmpty()) {

            StatisticsResponse statisticsResponse = this.statisticsService.getIntervalStatistics(ChosenRange.WEEK, workDays);
            if (statisticsResponse != null) {
                return ResponseEntity.ok(statisticsResponse);
            } else {
                System.err.println("week endpoint-> Condition not predicted - ");
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ErrorResponse("Internal server error.", "An error occurred, please try again later."));
            }

        } return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Work information not found.", "There is no work registry one week from now for the user with id: "+ userId+"."));

    }

    @GetMapping("/month")
    @Operation(summary = "Get this month's work information", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Work information retrieved.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StatisticsResponse.class))),
            @ApiResponse(responseCode = "404", description = "Work information not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<Object> month (@RequestParam("userId") int userId) {

        Date start = this.statisticsService.subtractDays(new Date(), 30), end = new Date();
        Set<DailyWorkInfo> workDays = this.statisticsRepository.findUserWorkByGivenInterval(userId, start, end);
        if (!workDays.isEmpty()) {

            StatisticsResponse statisticsResponse = this.statisticsService.getIntervalStatistics(ChosenRange.MONTH, workDays);
            if (statisticsResponse != null) {
                return ResponseEntity.ok(statisticsResponse);
            } else {
                System.err.println("week endpoint-> Condition not predicted - ");
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ErrorResponse("Internal server error.", "An error occurred, please try again later."));
            }

        } return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Work information not found.", "There is no work registry one month from now for the user with id: "+ userId+"."));

    }

    @GetMapping("/year")
    @Operation(summary = "Get this year's work information", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Work information retrieved.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StatisticsResponse.class))),
            @ApiResponse(responseCode = "404", description = "Work information not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<Object> year (@RequestParam("userId") int userId) {

        Date start = this.statisticsService.subtractDays(new Date(), 365), end = new Date();
        Set<DailyWorkInfo> workDays = this.statisticsRepository.findUserWorkByGivenInterval(userId, start, end);
        if (!workDays.isEmpty()) {

            StatisticsResponse statisticsResponse = this.statisticsService.getIntervalStatistics(ChosenRange.YEAR, workDays);
            if (statisticsResponse != null) {
                return ResponseEntity.ok(statisticsResponse);
            } else {
                System.err.println("week endpoint-> Condition not predicted - ");
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ErrorResponse("Internal server error.", "An error occurred, please try again later."));
            }

        } return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Work information not found.", "There is no work registry one year from now for the user with id: "+ userId+"."));

    }

    @GetMapping("/personalized")
    @Operation(summary = "Get date personalized work information", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Work information retrieved.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StatisticsResponse.class))),
            @ApiResponse(responseCode = "404", description = "Work information not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<Object> personalized (
            @RequestParam("userId") int userId,
            @RequestParam("start") @DateTimeFormat(pattern="yyyy-MM-dd") Date start,
            @RequestParam("end") @DateTimeFormat(pattern="yyyy-MM-dd") Date end
    ) {

        Set<DailyWorkInfo> workDays = this.statisticsRepository.findUserWorkByGivenInterval(userId, start, end);
        if (!workDays.isEmpty()) {

            StatisticsResponse statisticsResponse = this.statisticsService.getIntervalStatistics(ChosenRange.PERSONALIZED, workDays);
            if (statisticsResponse != null) {
                return ResponseEntity.ok(statisticsResponse);
            } else {
                System.err.println("week endpoint-> Condition not predicted - ");
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ErrorResponse("Internal server error.", "An error occurred, please try again later."));
            }

        } return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Work information not found.", "There is no work registry for the period requested to the user with id: "+ userId+"."));

    }


}
