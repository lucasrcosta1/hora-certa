package com.horacerta.api.service.conversion;

import lombok.Data;

@Data
public class TimeConversionInfo {

    private long seconds;
    private long minutes;
    private long hours;
    private long days;

    public TimeConversionInfo () {}
    public TimeConversionInfo (long seconds, long minutes, long hours, long days) {

        this.seconds = seconds;
        this.minutes = minutes;
        this.hours = hours;
        this.days = days;

    }

    @Override
    public String toString() {
        return "TimeConversionInfo{" +
                "seconds=" + seconds +
                ", minutes=" + minutes +
                ", hours=" + hours +
                ", days=" + days +
                '}';
    }

}
