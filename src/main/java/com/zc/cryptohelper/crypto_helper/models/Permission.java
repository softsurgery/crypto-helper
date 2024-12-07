package com.zc.cryptohelper.crypto_helper.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Size(max = 255)
    private String description;

    @ManyToMany(mappedBy = "permissions")
    @JsonIgnore
    private List<Role> roles;
}