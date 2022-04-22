package com.company.storehouse.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.company.storehouse.controller.response.View;
import com.company.storehouse.model.User;
import com.company.storehouse.model.enumeration.RoleName;
import com.company.storehouse.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
public class AccountController {

    private final UserService userService;

    @Autowired
    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Get current user's profile", notes = "Please, use token.")
    @GetMapping("/profile")
    @JsonView(View.Full.class)
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.ok(userService.current());
    }

    @ApiOperation(value = "Get current user's role", notes = "Please, use token.")
    @GetMapping("/current/role")
    @JsonView(View.Full.class)
    public ResponseEntity<RoleName> getCurrentUserRole(Principal principal) {
        return ResponseEntity.ok(userService.getCurrentUserRole(principal));
    }

}
