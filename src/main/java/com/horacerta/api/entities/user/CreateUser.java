package com.horacerta.api.entities.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUser {
    
    @Valid @NotNull @NotEmpty @Size(min=3, max=30)
    String firstName;
    @Valid @NotNull @NotEmpty @Size(min=3, max=30)
    String lastName;
    @Valid @NotNull @NotEmpty @Size(min=10, max=100)
    String email;
    @Valid @NotNull @NotEmpty @Size(min=11, max=11)
    String phone;
    @Valid @NotNull @NotEmpty @Size(min=8, max=30)
    String password;
   
}
