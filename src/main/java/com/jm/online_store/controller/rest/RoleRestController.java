package com.jm.online_store.controller.rest;

import com.jm.online_store.model.Role;
import com.jm.online_store.service.interf.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Api(description = "Rest controller for roles")
public class RoleRestController {

    private final RoleService roleService;

    @GetMapping
    @ApiOperation(value = "Get all roles",
            authorizations = { @Authorization(value = "jwtToken") })
    public List<Role> findAll() {
        return roleService.findAll();
    }
}
