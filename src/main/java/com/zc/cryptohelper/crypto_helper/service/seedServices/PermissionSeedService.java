package com.zc.cryptohelper.crypto_helper.service.seedServices;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zc.cryptohelper.crypto_helper.models.Permission;
import com.zc.cryptohelper.crypto_helper.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class PermissionSeedService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void seedPermissionsFromJson() {
        try (InputStream inputStream = getClass().getResourceAsStream("/permissions.json")) {
            List<Permission> permissions = objectMapper.readValue(inputStream, new TypeReference<List<Permission>>() {});
            for (Permission permission : permissions) {
                if (!permissionRepository.existsByName(permission.getName())) {
                    permissionRepository.save(permission);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load permissions from JSON file", e);
        }
    }
}
