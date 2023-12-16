package com.horacerta.api.controllers;

import com.horacerta.api.entities.response.ErrorResponse;
import com.horacerta.api.entities.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/create")
    @Operation(summary = "Create a new user", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was created, check your email to further steps", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> createUser (@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String phone, @RequestParam String password) {

        System.out.println("Should create user received through parameters: ");
        return null;
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

