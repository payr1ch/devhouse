package com.example.devhouse.user_things.registration;

import jakarta.persistence.Column;

public record RegistrationRequest(
        String email,
        String password,
        String username,
        String groups,
        int rank,
        String role) {

}
