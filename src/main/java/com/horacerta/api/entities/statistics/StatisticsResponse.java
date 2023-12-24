package com.horacerta.api.entities.statistics;

import com.horacerta.api.auxiliary.conversion.TimeConversionInfo;
import lombok.Data;

@Data
public class StatisticsResponse {

    private ChosenRange chosenRange;
    private TimeConversionInfo workedHours;
    private TimeConversionInfo missingOrAccumulatedHours;

    public StatisticsResponse () {}

    public StatisticsResponse (ChosenRange chosenRange) {
        this.chosenRange = chosenRange;
        this.workedHours = new TimeConversionInfo();
        this.missingOrAccumulatedHours = new TimeConversionInfo();
    }

    public StatisticsResponse (ChosenRange chosenRange, TimeConversionInfo workedHours, TimeConversionInfo missingOrAccumulatedHours) {
        this.chosenRange = chosenRange;
        this.workedHours = workedHours;
        this.missingOrAccumulatedHours = missingOrAccumulatedHours;
    }

}
