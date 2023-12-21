package com.horacerta.api.entities.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserWorkInfo {

    @Valid @NotNull @NotEmpty
    int id;
    @Valid @NotNull @NotEmpty @Size(min=3, max=30)
    String areaOfExpertise;
    @Valid @NotNull @NotEmpty
    int hoursWorkedDaily;
    @Valid @NotNull @NotEmpty
    int hoursWorkedWeekly;

}
