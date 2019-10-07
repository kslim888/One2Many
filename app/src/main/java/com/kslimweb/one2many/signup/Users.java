package com.kslimweb.one2many.signup;

public class Users {
    private String email, personRole;

    public Users(String email, String personRole) {
        this.email = email;
        this.personRole = personRole;
    }

    public String getEmail() {
        return email;
    }

    public String getPersonRole() {
        return personRole;
    }
}
