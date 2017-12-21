package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//	@Query("select from User where email = ?1")
	User findByEmail(String email);
}
