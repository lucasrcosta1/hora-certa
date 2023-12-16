package com.horacerta.api.entities.work;

import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Data
public class CheckInAtWork {

    @Valid
    int userId;
    @Valid
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date startedAt;
    @Valid
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date finishedAt;
    @Valid
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date lunchStartedAt;
    @Valid
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date lunchFinishedAt;

}
