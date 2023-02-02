package com.project.Bsmart.controller;


import com.project.Bsmart.entity.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/user")
public class UserController {
    @GetMapping
    public ResponseEntity<ApiResponse<String>> getAllBranch() {
        return ResponseEntity.ok(ApiResponse.success("test api"));
    }

}
