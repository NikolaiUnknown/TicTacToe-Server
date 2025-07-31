package com.tictactoe.server.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    

    @GetMapping
    public String test(){
        return "";
    }
}
