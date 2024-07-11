package com.tecnica.conexa.controller;

import com.tecnica.conexa.entity.AuthenticationRequest;
import com.tecnica.conexa.entity.AuthenticationResponse;
import com.tecnica.conexa.entity.User;
import com.tecnica.conexa.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioService userService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Credenciales incorrectas", e);
        }

        final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = userService.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registerUser(@RequestBody User usuario) {
        // Verificar si el usuario ya existe
        if (userService.loadUserByUsername(usuario.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El usuario ya existe");
        }

        User savedUser = userService.save(usuario);

        final String token = userService.generateToken(savedUser.getUsername());

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}