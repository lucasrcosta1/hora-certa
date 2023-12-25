package com.horacerta.api.entities.work;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import com.horacerta.api.auxiliary.conversion.TimeConversionInfo;
import jakarta.persistence.*;
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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lunch_started_at", nullable = true)
    private Date lunchStartedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lunch_finished_at", nullable = true)
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
     * Calculate the duration between startedAt and finishedAt
     * @return Duration
     */
    public Duration calculateDuration(Date start, Date end) {
        Instant startedInstant = start.toInstant();
        Instant finishedInstant = end.toInstant();

        return Duration.between(startedInstant, finishedInstant);

    }

    public TimeConversionInfo getDurationDetails(Duration duration) {
        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        return new TimeConversionInfo(seconds, minutes, hours, days);
    }

    /**
     * Convert the time difference.
     * @param timeDifference long
     * @return TimeConversionInfo
     */
    private TimeConversionInfo _convertTimeDifference(long timeDifference) {

        long seconds = timeDifference / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        return new TimeConversionInfo(seconds, minutes, hours, days);

    }
}
