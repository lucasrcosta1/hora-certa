package com.horacerta.api.controllers;

import com.horacerta.api.entities.response.ErrorResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

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
    public ResponseEntity<Object> day (int userId) {

        System.out.println(new Date());
        DailyWorkInfo userWorkRegistry = this.statisticsRepository.findUserWorkByGivenDay(userId, new Date());
        if (userWorkRegistry != null) {
            StatisticsResponse statisticsResponse = this.statisticsService.getDayWorkInfo(userWorkRegistry);
            if (statisticsResponse != null) {
                return ResponseEntity.ok(statisticsResponse);
            }
            System.err.println("day endpoint-> Condition not predicted - ");
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ErrorResponse("Internal server error.", "An error occurred, please try again later."));
        } return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Work information not found.", "There is no work registry today for the user with id: "+ userId+"."));
    }


}
