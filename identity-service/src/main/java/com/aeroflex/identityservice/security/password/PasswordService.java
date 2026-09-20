package com.aeroflex.identityservice.security.password;

public interface PasswordService {

    String encode(String rawPassword);

    boolean matches(String rawPassword,
                    String encodedPassword);

}
