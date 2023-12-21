package com.horacerta.api.controllers;

import com.horacerta.api.entities.response.ErrorResponse;
import com.horacerta.api.entities.response.SuccessResponse;
import com.horacerta.api.entities.user.CreateUser;
import com.horacerta.api.entities.user.User;
import com.horacerta.api.entities.user.UserWorkInfo;
import com.horacerta.api.repositories.UserRepository;
import com.horacerta.api.service.hashPwd.HashPwd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController implements Controller {

    UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // TODO: 20/12/23  Send e-mail to the user registered.
    @PostMapping(path = "/create", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @Operation(summary = "Create a new user", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully created ", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "409", description = "User was created already", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "Server internal error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> createUser (@Valid @ModelAttribute CreateUser createUser) {

        try {

            createUser.setPassword(_createPasswordHash(createUser.getPassword()));
            if (userRepository.registerNewUser(createUser.getFirstName(), createUser.getLastName(), createUser.getEmail(), createUser.getPassword(), createUser.getPhone(), new Date()) > 0)
                return ResponseEntity.ok(new SuccessResponse("User created.", "User created with success. Check your email to further steps."));
            else
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("User not created.", "User with email " + createUser.getEmail() + " was already created."));

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Hash couldn't be created: "+ e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ErrorResponse("Server error.", "An internal error ocurred."));
    }


    // TODO: 20/12/23  Send token to front-end.
    @PutMapping(path = "/register-work-info")
    @Operation(summary = "Register user's work info.", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Work info registered with success.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> registerUserWorkInfo (@Valid @RequestBody UserWorkInfo userWorkInfo) {

        if (userRepository.registerUserWorkInfo(userWorkInfo.getId(), userWorkInfo.getAreaOfExpertise(), userWorkInfo.getHoursWorkedDaily(), userWorkInfo.getHoursWorkedWeekly()) > 0)
            return ResponseEntity.ok(new SuccessResponse("User's work information registered.", "User work info was registered with success. You may now use the app."));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("User not found.", "User with id " + userWorkInfo.getId() + " was not found."));

    }


    @GetMapping("/all")
    public List<User> getAllUsers () {

        return userRepository.findAll();

    }


    @GetMapping("/filter-by-email")
    public User getUserByGivenEmail (@RequestParam String email) {

        return userRepository.findByEmail(email);

    }

    private String _createPasswordHash(String password) throws NoSuchAlgorithmException {

        HashPwd hashPwd = new HashPwd(password);
        return HashPwd.bytesToHex(hashPwd.getHashbytes());

    }




//    @PostMapping("/auth/remove")
//    @Operation(summary = "Create a new user", method = "POST")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "User was created, check your email to further steps", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))),
//            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
//            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
//    })
//    public ResponseEntity<Object> removeUser (@RequestParam String token, @RequestParam String id) {
//
//        System.out.println("Should remove user received through parameters if token is real: ");
//        return null;
//    }

}

