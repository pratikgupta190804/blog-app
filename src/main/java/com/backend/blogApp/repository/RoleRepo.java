package com.backend.blogApp.repository;

import com.backend.blogApp.entity.Role;
import com.backend.blogApp.entity.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {

    Optional<Role> findByRole(RoleEnum roleEnum);
}
