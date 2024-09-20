package com.example.klantenportaal.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "refreshToken")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token")
    private String token;

    @Column(name = "used")
    private boolean used;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "parent_refresh_token_id")
    private RefreshToken parentRefreshToken;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "child_refresh_token_id")
    private RefreshToken childRefreshToken;

    public RefreshToken(String token, boolean used, RefreshToken parentRefreshToken, RefreshToken childRefreshToken) {
        this.token = token;
        this.used = used;
        this.parentRefreshToken = parentRefreshToken;
        this.childRefreshToken = childRefreshToken;
    }
}