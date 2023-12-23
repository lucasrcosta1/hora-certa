package com.horacerta.api.service;

import com.horacerta.api.auxiliary.conversion.TimeConversionInfo;
import com.horacerta.api.entities.statistics.ChosenRange;
import com.horacerta.api.entities.statistics.StatisticsResponse;
import com.horacerta.api.entities.user.User;
import com.horacerta.api.entities.work.DailyWorkInfo;
import com.horacerta.api.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class StatisticsService {

    private static final TimeConversionInfo GRANTED_INTERVAL = new TimeConversionInfo(0, 15, 0, 0);
    UserRepository userRepository;

    public StatisticsService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public StatisticsResponse getDayWorkInfo (DailyWorkInfo workday) {

        return new StatisticsResponse(
            ChosenRange.DAY,
            this.calculateWorkTime(workday),
            this.calculateMissingOrAccumulatedTime(workday)
        );

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

}
