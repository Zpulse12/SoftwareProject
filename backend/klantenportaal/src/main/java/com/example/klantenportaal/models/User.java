package com.example.klantenportaal.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;
import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, length = 100, nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "vat")
    private String vat;

    @Column(name = "eori")
    private String eori;

    @Column(nullable = true)
    private String companyCode;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "house_number")
    private String houseNumber;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "role")
    private String role;

    public User(String fullName, String email, String password, String companyCode, String vat, String eori,
                String phoneNumber, String companyName, String streetName, String houseNumber, String city, String country,
                Status status, String role) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.vat = vat;
        this.eori = eori;
        this.companyCode = companyCode;
        this.companyName = companyName;
        this.phoneNumber = phoneNumber;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.city = city;
        this.country = country;
        this.status = status;
        this.role = role;
    }

    public enum Status {
        APPROVED,
        PENDING,
        DECLINED
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    public boolean isApproved() {
        return Status.APPROVED.equals(this.status);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isApproved();
    }

    @Override
    public String toString() {
        return "User(" +
                "id=" + id +
                ", fullName=" + fullName +
                ", email=" + email +
                ", vat=" + vat +
                ", eori=" + eori +
                ", companyCode=" + companyCode +
                ", companyName=" + companyName +
                ", phoneNumber=" + phoneNumber +
                ", streetName=" + streetName +
                ", houseNumber=" + houseNumber +
                ", city=" + city +
                ", country=" + country +
                ", status=" + status +
                ", role=" + role +
                ")";
    }
}
