package com.zc.cryptohelper.crypto_helper.models;

import com.zc.cryptohelper.crypto_helper.storage.Upload;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
public class Coin {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(max = 100)
    private String hrefName;

    @OneToOne
    @JoinColumn(name = "upload_id")  // Foreign key column in the Coin table
    private Upload upload;

    @ManyToMany(mappedBy = "favouriteCoins")
    private Set<User> usersWhoFavorited;
}
