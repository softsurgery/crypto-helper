package com.zc.cryptohelper.crypto_helper.models;

import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(updatable = false)
    private String name;

    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles;
}