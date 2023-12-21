package com.horacerta.api.entities.work;

import java.util.Date;

import com.horacerta.api.service.conversion.TimeConversionInfo;
import lombok.Data;

@Data
public class DailyWorkInfo {

    private int  id;
    private int userId;
    private Date startedAt;
    private Date finishedAt;
    private Date lunchStartedAt;
    private Date lunchFinishedAt;
    private boolean isDayOff;
    private boolean isVacation;
    private Date createdAt;

    /**
     * Calculate exceeding hours for the day.
     * @implNote Return can be negative depending on how many hours user worked.
     * @param hoursWorkedDaily int
     * @return long
     */
    public long calculateExceedingHoursForToday (int hoursWorkedDaily) {

        long workedHoursDifference = finishedAt.getTime() - startedAt.getTime();
        TimeConversionInfo workedTimeTimerConversionInfo = _convertTimeDifference(workedHoursDifference);
        if (workedTimeTimerConversionInfo.getHours() != hoursWorkedDaily) return workedTimeTimerConversionInfo.getHours() - hoursWorkedDaily;
        else return 0;

    }

    /**
     * Calculate exceeding minutes for the day discounting number of hours exceeded already.
     * @implNote Return can be negative depending on how many minutes user worked.
     * @param hoursWorkedDaily int
     * @return long
     */
    public long calculateExceedingMinutesForToday (int hoursWorkedDaily, long hoursExceeded) {

        int minutesWorkedDaily = hoursWorkedDaily * 60;
        long workedHoursDifference = finishedAt.getTime() - startedAt.getTime();
        TimeConversionInfo workedTimeTimerConversionInfo = _convertTimeDifference(workedHoursDifference);
        if (workedTimeTimerConversionInfo.getMinutes() != minutesWorkedDaily) return workedTimeTimerConversionInfo.getMinutes() - minutesWorkedDaily - hoursExceeded * 60;
        else return 0;

    }

    /**
     * Convert the time difference.
     * @param timeDifferenceLunch long
     * @return TimeConversionInfo
     */
    private TimeConversionInfo _convertTimeDifference(long timeDifferenceLunch) {

        long secondsLunch = timeDifferenceLunch / 1000;
        long minutesLunch = secondsLunch / 60;
        long hoursLunch = minutesLunch / 60;
        long daysLunch = hoursLunch / 24;
        return new TimeConversionInfo(secondsLunch, minutesLunch, hoursLunch, daysLunch);

    }
}
