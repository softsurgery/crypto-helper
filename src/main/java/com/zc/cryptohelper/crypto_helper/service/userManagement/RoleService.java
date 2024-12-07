package com.zc.cryptohelper.crypto_helper.service.userManagement;

import com.zc.cryptohelper.crypto_helper.models.Permission;
import com.zc.cryptohelper.crypto_helper.models.Role;
import com.zc.cryptohelper.crypto_helper.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Page<Role> findRoles(int page, int size, String searchTerm, String sortKey, String order) {
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortKey));
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return roleRepository.findByNameContainingIgnoreCase(searchTerm, pageRequest);
        }
        return roleRepository.findAll(pageRequest);
    }


    // Fetch all roles
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Get a role by ID
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    // Create or update a role
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    // Delete a role by ID
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}