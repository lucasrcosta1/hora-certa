package com.horacerta.api.service;

import com.horacerta.api.auxiliary.conversion.TimeConversionInfo;
import com.horacerta.api.entities.statistics.ChosenRange;
import com.horacerta.api.entities.statistics.StatisticsResponse;
import com.horacerta.api.entities.user.User;
import com.horacerta.api.entities.work.DailyWorkInfo;
import com.horacerta.api.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
public class StatisticsService {

    private static final TimeConversionInfo GRANTED_INTERVAL = new TimeConversionInfo(0, 15, 0, 0);
    UserRepository userRepository;

    public StatisticsService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public StatisticsResponse getDailyStatistics (DailyWorkInfo workday) {

        TimeConversionInfo missingOrAccumulatedTime =  this.checkWhetherVacationOrDayOff(workday);
        return new StatisticsResponse(
            ChosenRange.DAY,
            workday.isDayOff() ? new TimeConversionInfo() : this.calculateWorkTime(workday),
            missingOrAccumulatedTime
        );

    }

    private TimeConversionInfo checkWhetherVacationOrDayOff(DailyWorkInfo workday) {

        if (workday.isDayOff()) {

            return new TimeConversionInfo(0,0,0,-1);

        } else if (workday.isVacation()) {

            int vacationDays = this.howManyVacationDays(workday);
            return new TimeConversionInfo(0,0,0, vacationDays);

        } else {

            return this.calculateMissingOrAccumulatedTime(workday);

        }

    }

    private int howManyVacationDays(DailyWorkInfo workday) {

        int entireDay = 1;
        Duration vacationDuration = workday.calculateDuration(workday.getStartedAt(), workday.getFinishedAt());
        TimeConversionInfo vacationTime = workday.getDurationDetails(vacationDuration);
        return - ((int)vacationTime.getDays() + entireDay);

    }

    public Date subtractDays(Date date, int days) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, -days);
        return calendar.getTime();

    }

    public StatisticsResponse getIntervalStatistics (ChosenRange chosenRange, Set<DailyWorkInfo> workDays) {

        StatisticsResponse statisticsResponse = new StatisticsResponse(chosenRange);
        for (DailyWorkInfo workDay : workDays) {

            if (workDay != null) {

                TimeConversionInfo missingOrAccumulatedTime =  this.checkWhetherVacationOrDayOff(workDay);
                statisticsResponse.setWorkedHours(this.addTime(statisticsResponse.getWorkedHours(), (workDay.isDayOff() || workDay.isVacation()) ? new TimeConversionInfo() : this.calculateWorkTime(workDay)));
                statisticsResponse.setMissingOrAccumulatedHours(this.addTime(statisticsResponse.getMissingOrAccumulatedHours(), missingOrAccumulatedTime));

            } else { return null; }

        }
        return statisticsResponse;

    }

    private TimeConversionInfo calculateWorkTime(DailyWorkInfo workday) {

        Duration workDayDuration = workday.calculateDuration(workday.getStartedAt(), workday.getFinishedAt());
        Duration lunchDuration = workday.calculateDuration(workday.getLunchStartedAt(), workday.getLunchFinishedAt());
        TimeConversionInfo detailsWorkDay = workday.getDurationDetails(workDayDuration);
        TimeConversionInfo detailsLunch = workday.getDurationDetails(lunchDuration);
        if (detailsLunch.equals(GRANTED_INTERVAL))
            detailsLunch = new TimeConversionInfo();
        return new TimeConversionInfo(
            detailsWorkDay.getSeconds() - detailsLunch.getSeconds(),
            detailsWorkDay.getMinutes() - detailsLunch.getMinutes(),
            detailsWorkDay.getHours() - detailsLunch.getHours(),
            detailsWorkDay.getDays() - detailsLunch.getDays()
        );

    }

    private TimeConversionInfo calculateMissingOrAccumulatedTime(DailyWorkInfo workday) {

        Integer hoursSupposedToWork = this.getDayWorkedHoursAverageForUser(workday.getUserId());
        if (hoursSupposedToWork != null) {

            TimeConversionInfo workedTime = calculateWorkTime(workday);
            workedTime.setHours(workedTime.getHours() - hoursSupposedToWork);
            return workedTime;

        } return null;


    }

    private Integer getDayWorkedHoursAverageForUser (int userId) {

        Optional<User> user = this.userRepository.findById(userId);
        return user.map(User::getHoursWorkedDaily).orElse(null);

    }

    private Integer getWeekWorkedHoursAverageForUser (int userId) {

        Optional<User> user = this.userRepository.findById(userId);
        return user.map(User::getHoursWorkedWeekly).orElse(null);

    }

    /**
     * Sum two TimeConversionInfo structures.
     * @param actualAmount TimeConversionInfo
     * @param amountToBeAdded TimeConversionInfo
     * @return TimeConversionInfo
     */
    private TimeConversionInfo addTime(TimeConversionInfo actualAmount, TimeConversionInfo amountToBeAdded) {
        long totalSeconds = actualAmount.getSeconds() + amountToBeAdded.getSeconds();
        long totalMinutes = actualAmount.getMinutes() + amountToBeAdded.getMinutes();
        long totalHours = actualAmount.getHours() + amountToBeAdded.getHours();
        long totalDays = actualAmount.getDays() + amountToBeAdded.getDays();

        if (totalSeconds >= 60) {
            totalMinutes += totalSeconds / 60;
            totalSeconds %= 60;
        }

        if (totalMinutes >= 60) {
            totalHours += totalMinutes / 60;
            totalMinutes %= 60;
        }

        if (totalHours >= 8) {
            totalDays += totalHours / 8;
            totalHours %= 8;
        }

        return new TimeConversionInfo(totalSeconds, totalMinutes, totalHours, totalDays);
    }



}
