package com.pws.usermanagement.service;

import com.pws.usermanagement.dto.RoleDTO;
import com.pws.usermanagement.dto.SignUpDTO;
import com.pws.usermanagement.entity.Role;
import com.pws.usermanagement.entity.User;
import com.pws.usermanagement.entity.UserRoleXref;
import com.pws.usermanagement.repo.RoleRepository;
import com.pws.usermanagement.repo.UserRepository;
import com.pws.usermanagement.repo.UserRoleXrefRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {


    private final UserRepository userRepository;

    private final UserRoleXrefRepository userRoleXrefRepository;

    private final RoleRepository roleRepository;

    @Override
    public void signUp(SignUpDTO signUpDTO) throws Exception {

        if (!isStrongPassword(signUpDTO.getPassword())) {
            throw new Exception("Please enter strong password, at least one uppercase letter, one lowercase letter, one digit, and one special character needed");
        }
        Optional<User> optionalUser = userRepository.findByEmail(signUpDTO.getEmail());
        List<UserRoleXref> userRoleXrefs = null;
        if ((optionalUser.isPresent())) {
            User user = optionalUser.get();
            userRoleXrefs = userRoleXrefRepository.findByUserId(user.getId());
            boolean isPoster = false;
            boolean isApplier = false;

            for (UserRoleXref userRoleXref : userRoleXrefs) {
                Role role = userRoleXref.getRole();
                if (role.getName().equalsIgnoreCase("poster")) {
                    isPoster = true;
                } else if (role.getName().equalsIgnoreCase("applier")) {
                    isApplier = true;
                }
            }
            if (isPoster && isApplier) {
                throw new Exception("User exist with bit role with this email : " + signUpDTO.getEmail());
            } else if ((isPoster && signUpDTO.getRole().equalsIgnoreCase("poster")) || (isApplier && signUpDTO.getRole().equalsIgnoreCase("applier"))) {
                throw new Exception("User already exist with role with this email : " + signUpDTO.getEmail());
            } else {
                Optional<Role> roleOptional = roleRepository.findRoleByName(signUpDTO.getRole());

                if (!roleOptional.isPresent()) {
                    throw new Exception("Role not found : " + signUpDTO.getRole());
                }

                UserRoleXref newUserRoleXref = new UserRoleXref();
                newUserRoleXref.setUser(user);
                newUserRoleXref.setRole(roleOptional.get());
                newUserRoleXref.setActive(true);

                if (isPoster && signUpDTO.getRole().equalsIgnoreCase("poster")) {
                    for (UserRoleXref userRoleXref : userRoleXrefs) {
                        if (userRoleXref.getRole().getName().equalsIgnoreCase("applier")) {
                            userRoleXref.setActive(false);
                            userRoleXrefRepository.save(userRoleXref);
                        }
                    }
                } else if (isPoster && signUpDTO.getRole().equalsIgnoreCase("applier")) {
                    for (UserRoleXref userRoleXref : userRoleXrefs) {
                        if (userRoleXref.getRole().getName().equalsIgnoreCase("poster")) {
                            userRoleXref.setActive(false);
                            userRoleXrefRepository.save(userRoleXref);
                        }
                    }
                }
                userRoleXrefRepository.save(newUserRoleXref);
            }
        } else {
            User user = new User();
            user.setDateOfBirth(signUpDTO.getDateOfBirth());
            user.setFirstName(signUpDTO.getFirstName());
            user.setLastName(signUpDTO.getLastName());
            user.setEmail(signUpDTO.getEmail().toLowerCase());
            user.setPhoneNumber(signUpDTO.getPhoneNumber());
            user.setGender(signUpDTO.getGender());
            String userId = user.getEmail();

            PasswordEncoder encoder = new BCryptPasswordEncoder(8);
            user.setPassword(encoder.encode(signUpDTO.getPassword()));

            UserRoleXref userRoleXref = new UserRoleXref();
            userRoleXref.setUser(user);
            Optional<Role> roleOptional = roleRepository.findRoleByName(signUpDTO.getRole());
            if (!roleOptional.isPresent()) {
                throw new Exception("Role not found: " + signUpDTO.getRole());
            } else {
                userRoleXref.setRole(roleOptional.get());
                userRoleXref.setActive(true);
                userRepository.save(user);
                userRoleXrefRepository.save(userRoleXref);
            }
        }
    }


    @Override
    public void addRole( RoleDTO roleDTO) throws Exception {
        String roleName = roleDTO.getName().toLowerCase();
        if (!roleRepository.findRoleByName(roleName).isPresent()) {
            Role role = new Role();
            role.setName(roleName);
            role.setActive(roleDTO.isActive());
            roleRepository.save(role);
        } else {
            throw new Exception("Role with name " + roleDTO.getName() + " already exists" );
        }
    }









    private boolean isStrongPassword(String password) {
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isUpperCase(ch)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(ch)) {
                hasLowercase = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (isSpecialChar(ch)) {
                hasSpecialChar = true;
            }
        }

        return password.length() >= 8 && hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }

    private boolean isSpecialChar(char ch) {
        String specialChars = "!@#$%^&*()_-+=[{]};:<>|./?";
        return specialChars.contains(Character.toString(ch));
    }

}


