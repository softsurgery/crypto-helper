package com.zc.cryptohelper.crypto_helper.service.userManagement;

import com.zc.cryptohelper.crypto_helper.models.Permission;
import com.zc.cryptohelper.crypto_helper.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }


    public Page<Permission> findPermissions(int page, int size, String searchTerm, String sortKey, String order) {
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortKey));
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return permissionRepository.findByNameContainingIgnoreCase(searchTerm, pageRequest);
        }
        return permissionRepository.findAll(pageRequest);
    }

    // Fetch all permissions
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    // Get a permission by ID
    public Optional<Permission> getPermissionById(Long id) {
        return permissionRepository.findById(id);
    }

    // Create or update a permission
    public Permission savePermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    // Delete a permission by ID
    public void deletePermission(Long id) {
        permissionRepository.deleteById(id);
    }
}