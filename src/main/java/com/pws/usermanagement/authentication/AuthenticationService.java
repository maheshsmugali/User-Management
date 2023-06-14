package com.pws.usermanagement.authentication;

import com.pws.usermanagement.entity.Role;
import com.pws.usermanagement.entity.Token;
import com.pws.usermanagement.entity.TokenType;
import com.pws.usermanagement.entity.User;
import com.pws.usermanagement.repo.TokenRepository;
import com.pws.usermanagement.repo.UserRepository;
import com.pws.usermanagement.repo.UserRoleXrefRepository;
import com.pws.usermanagement.utility.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final UserRoleXrefRepository userRoleXrefRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new Exception("Invalid email/password supplied");
        }

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new Exception("User not found"));

        List<Role> roles = userRoleXrefRepository.findUserRoleByUserId(user.getId());

        if (roles.isEmpty()) {
            throw new Exception("Roles not found for the user");
        }

        String jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }


}
