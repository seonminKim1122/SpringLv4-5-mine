package com.sparta.springlv4.repository;

import com.sparta.springlv4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
