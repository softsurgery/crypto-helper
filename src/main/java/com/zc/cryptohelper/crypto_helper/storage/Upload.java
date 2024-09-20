package com.zc.cryptohelper.crypto_helper.storage;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Upload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private String slug;

    @Column()
    private String filename;

    @Column()
    private String relativePath;

    @Column()
    private String mimetype;

    @Column()
    private Long size;
}