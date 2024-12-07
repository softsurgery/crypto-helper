package com.zc.cryptohelper.crypto_helper.repository;

import com.zc.cryptohelper.crypto_helper.models.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    Page<Role> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Optional<Role> findByName(String name);
}