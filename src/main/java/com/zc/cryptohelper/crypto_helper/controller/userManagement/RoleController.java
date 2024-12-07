package com.zc.cryptohelper.crypto_helper.controller.userManagement;

import com.zc.cryptohelper.crypto_helper.models.Permission;
import com.zc.cryptohelper.crypto_helper.models.Role;
import com.zc.cryptohelper.crypto_helper.service.userManagement.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // Get all roles
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/list")
    public ResponseEntity<Page<Role>> findRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String order
    ) {
        Page<Role> roles = roleService.findRoles(page, size, search, sort, order);
        return ResponseEntity.ok(roles);
    }

    // Get role by ID
    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new role
    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.saveRole(role));
    }

    // Update an existing role
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role updatedRole) {
        return roleService.getRoleById(id)
                .map(existingRole -> {
                    existingRole.setName(updatedRole.getName());
                    existingRole.setDescription(updatedRole.getDescription());
                    existingRole.setPermissions(updatedRole.getPermissions());
                    return ResponseEntity.ok(roleService.saveRole(existingRole));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete a role by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        if (roleService.getRoleById(id).isPresent()) {
            roleService.deleteRole(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}