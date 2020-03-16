package io.cityos.cityosair.data.model;

/**
 * Created by Andrej on 07/02/2017.
 */

public class AuthCredentials {
    private String email;
    private String password;

    public AuthCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
