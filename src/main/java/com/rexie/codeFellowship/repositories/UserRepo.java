package com.rexie.codeFellowship.repositories;

import com.rexie.codeFellowship.models.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<ApplicationUser, Long> {
    ApplicationUser findByUsername(String username);
}
