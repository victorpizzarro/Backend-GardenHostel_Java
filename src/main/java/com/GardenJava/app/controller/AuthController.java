package com.GardenJava.app.controller;

import com.GardenJava.app.dto.login.LoginRequestDTO;
import com.GardenJava.app.dto.login.RegistroRequestDTO;
import com.GardenJava.app.dto.login.ResponseDTO;
import com.GardenJava.app.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody @Valid LoginRequestDTO body) {
        return ResponseEntity.ok(authService.login(body));
    }


    @PostMapping("/registro")
    public ResponseEntity<ResponseDTO> registro(@RequestBody @Valid RegistroRequestDTO body) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.registrar(body));
    }


    @GetMapping("/me")
    public ResponseEntity<ResponseDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(authService.getUsuarioAtual(userDetails.getUsername()));
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {

        return ResponseEntity.ok().build();
    }
}