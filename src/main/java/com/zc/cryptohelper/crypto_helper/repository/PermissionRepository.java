package com.zc.cryptohelper.crypto_helper.repository;

import com.zc.cryptohelper.crypto_helper.models.Permission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends CrudRepository<Permission, Long> {
    Optional<Permission> findByName(String name);
}
