package com.jm.online_store.controller.rest;

import com.jm.online_store.model.User;
import com.jm.online_store.service.interf.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping(value = "/api/users")
@RequiredArgsConstructor
@Slf4j
@Api(description = "Rest controller for users")
public class UserRestController {

    private final UserService userService;

    @GetMapping
    @ApiOperation(value = "Get all users",
            authorizations = { @Authorization(value = "jwtToken") })
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    @ApiOperation(value = "Save user",
            authorizations = { @Authorization(value = "jwtToken") })
    public User saveUser(@RequestBody User user) {
        userService.addUser(user);
        return user;
    }

    @PutMapping
    @ApiOperation(value = "Update users",
            authorizations = { @Authorization(value = "jwtToken") })
    public User updateUser(@RequestBody User user) {
        userService.updateUserAdminPanel(user);
        return user;
    }

    @DeleteMapping(value = "/{userId}")
    @ApiOperation(value = "Delete user by ID",
            authorizations = { @Authorization(value = "jwtToken") })
    public ResponseEntity<Void> deleteUserById(@PathVariable Long userId) {
        userService.deleteByID(userId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
