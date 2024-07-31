package com.blogger.blogger.controllers;

import com.blogger.blogger.dto.request.UserRequestDTO;
import com.blogger.blogger.dto.response.GenericResponse;
import com.blogger.blogger.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<GenericResponse> login(@RequestParam String email, @RequestParam String password) {
        GenericResponse response = userService.login(email, password);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<GenericResponse> createUser(@RequestBody UserRequestDTO userDto) {
        GenericResponse response = userService.create(userDto);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

}
