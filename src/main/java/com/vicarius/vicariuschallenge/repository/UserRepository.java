package com.vicarius.vicariuschallenge.repository;

import com.vicarius.vicariuschallenge.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
