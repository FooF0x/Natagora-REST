package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRepository extends JpaRepository<Password, Long> {
}
