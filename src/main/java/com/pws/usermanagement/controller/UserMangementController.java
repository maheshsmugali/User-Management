package com.pws.usermanagement.controller;

import com.pws.usermanagement.config.ApiSuccess;
import com.pws.usermanagement.dto.RoleDTO;
import com.pws.usermanagement.dto.SignUpDTO;
import com.pws.usermanagement.service.UserManagementService;
import com.pws.usermanagement.utility.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserMangementController {

    private final UserManagementService service;

    @PostMapping("public/signup")
    public ResponseEntity<Object> signup(@RequestBody SignUpDTO signUpDTO) throws Exception {
        service.signUp(signUpDTO);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, "User registered successfully"));

    }

    @PostMapping("/public/role")
    public ResponseEntity<Object> addRole(@RequestBody RoleDTO roleDTO) throws Exception {
        service.addRole(roleDTO);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, "Role added successfully"));
    }


}
