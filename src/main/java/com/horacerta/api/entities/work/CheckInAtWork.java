package com.horacerta.api.entities.work;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Data
public class CheckInAtWork {

    @Valid
    @NotNull
    private int userId;

    @Valid
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC+3")
    @Schema(type = "string", pattern = "yyyy-MM-dd HH:mm:ss", example = "yyyy-MM-dd HH:mm:ss")
    private Date startedAt;

    @Valid
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC+3")
    @Schema(type = "string", pattern = "yyyy-MM-dd HH:mm:ss", example = "yyyy-MM-dd HH:mm:ss")
    private Date finishedAt;

    @Valid
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC+3")
    @Schema(type = "string", pattern = "yyyy-MM-dd HH:mm:ss", example = "yyyy-MM-dd HH:mm:ss")
    private Date lunchStartedAt;

    @Valid
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC+3")
    @Schema(type = "string", pattern = "yyyy-MM-dd HH:mm:ss", example = "yyyy-MM-dd HH:mm:ss")
    private Date lunchFinishedAt;

    @Valid
    @NotNull
    private boolean isDayOff;

    @Valid
    @NotNull
    private boolean isVacation;
}
