package com.zc.cryptohelper.crypto_helper.repository;

import com.zc.cryptohelper.crypto_helper.models.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> , JpaSpecificationExecutor<Permission> {
    Page<Permission> findByNameContainingIgnoreCase(String name, Pageable pageable);
    boolean existsByName(String name);
}
