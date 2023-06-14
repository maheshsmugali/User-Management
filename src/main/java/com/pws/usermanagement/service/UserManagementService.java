package com.pws.usermanagement.service;

import com.pws.usermanagement.dto.RoleDTO;
import com.pws.usermanagement.dto.SignUpDTO;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserManagementService {
    void signUp(SignUpDTO signUpDTO) throws Exception;

    void addRole(@RequestBody RoleDTO roleDTO) throws Exception;
}
