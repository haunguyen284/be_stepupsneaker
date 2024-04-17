package com.ndt.be_stepupsneaker.entity.employee;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.infrastructure.constant.EmployeeStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Table(name = "employee")
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Employee extends PrimaryEntity implements UserDetails {
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;

    @Column(name = "full_name", length = EntityProperties.LENGTH_NAME, nullable = false)
    @Nationalized
    private String fullName;

    @Column(name = "email", length = EntityProperties.LENGTH_EMAIL, nullable = false)
    private String email;

    @Column(name = "password", length = EntityProperties.LENGTH_PASSWORD, nullable = false)
    private String password;

    @Column(name = "status")
    private EmployeeStatus status;

    @Column(name = "address", length = EntityProperties.LENGTH_ADDRESS)
    @Nationalized
    private String address;

    @Column(name = "gender", length = EntityProperties.LENGTH_GENDER)
    @Nationalized
    private String gender;

    @Column(name = "phone_number", length = EntityProperties.LENGTH_PHONE)
    private String phoneNumber;

    @Column(name = "url_image")
    private String image;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getName()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
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
        return true;
    }
}
