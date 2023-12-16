package com.horacerta.api.entities.work;

import java.util.Date;

import com.horacerta.api.entities.User;
import com.horacerta.api.entities.conversion.TimeConversionInfo;
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
     * Calculate number of hours worked.
     * @implNote If lunch time is bigger than 15 minutes, the exceeded lunch time should be discounted from the work time.
     * @return int
     */
    public int calculateWorkHoursForToday () {

//        long timeDifferenceLunch = lunchFinishedAt.getTime() - lunchStartedAt.getTime();
//        long workedHoursDifference = finishedAt.getTime() - startedAt.getTime();

//        TimeConversionInfo lunchTimerConversionInfo = _convertTimeDifference(timeDifferenceLunch);
//        TimeConversionInfo workedTimeTimerConversionInfo = _convertTimeDifference(workedHoursDifference);
//
//        System.out.println("Time Difference in milliseconds: " + timeDifferenceLunch);
//        System.out.println("Time Difference in seconds: " + lunchTimerConversionInfo);

//        if (lunchTimerConversionInfo.getHours() == 0 && lunchTimerConversionInfo.getMinutes() <= 15) {
//
////            int lunchTime =
//            //if ()
//            System.out.println("less than 15min\nhours worked: "+ workedTimeTimerConversionInfo.toString());
//
//        } else {
//            System.out.println("more than 15min\nhours worked: "+ workedTimeTimerConversionInfo.toString());
//
//        }
        long resultHour = calculateExceedingHoursForToday(6);
        System.out.println("Exceeding hours: "+resultHour);
        long resultMinutes = calculateExceedingMinutesForToday(6, (int)resultHour);
        System.out.println("Exceeding minutes: "+resultMinutes);
        return 1;

    }


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
    public long calculateExceedingMinutesForToday (int hoursWorkedDaily, int hoursExceeded) {

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
