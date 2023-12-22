package com.horacerta.api.entities.work;

import java.util.Date;

import com.horacerta.api.service.conversion.TimeConversionInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "daily_work_info", schema = "public")
public class DailyWorkInfo {

    @Id
    @Column(name = "id")
    private int  id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "started_at")
    private Date startedAt;

    @Column(name = "finished_at")
    private Date finishedAt;

    @Column(name = "lunch_started_at")
    private Date lunchStartedAt;

    @Column(name = "lunch_finished_at")
    private Date lunchFinishedAt;

    @Column(name = "is_day_off")
    private boolean isDayOff;

    @Column(name = "is_vacation")
    private boolean isVacation;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

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
