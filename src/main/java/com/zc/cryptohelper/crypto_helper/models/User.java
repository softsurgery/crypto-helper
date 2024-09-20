package com.zc.cryptohelper.crypto_helper.models;

import com.zc.cryptohelper.crypto_helper.models.audit.DateAudit;
import jakarta.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class User extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required.")
    @Size(max = 15)
    private String username;

    @Email
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(max = 255)
    private String password;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToMany
    @JoinTable(
            name = "user_favourite_coins",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "coin_id")
    )
    private Set<Coin> favouriteCoins;
}

