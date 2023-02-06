package com.project.Bsmart.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_MEMBER') or hasRole('ROLE_MENTOR') or hasRole('ROLE_ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('ROLE_MENTOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/member")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public String adminAccess() {
        return "Member Board.";
    }
}