package com.ndt.be_stepupsneaker.entity.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.entity.cart.Cart;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import com.ndt.be_stepupsneaker.infrastructure.constant.CustomerStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Table(name = "customer")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends PrimaryEntity implements UserDetails {
    @Column(name = "full_name", length = EntityProperties.LENGTH_NAME)
    @Nationalized
    private String fullName;

    @Column(name = "email", length = EntityProperties.LENGTH_EMAIL, nullable = false, unique = true)
    private String email;

    @Column(name = "date_of_birth")
    private Long dateOfBirth;

    @Column(name = "password", length = EntityProperties.LENGTH_PASSWORD, nullable = false)
    private String password;

    @Column(name = "status")
    private CustomerStatus status;

    @Column(name = "gender", length = EntityProperties.LENGTH_GENDER)
    @Nationalized
    private String gender;

    @Column(name = "url_image")
    private String image;

    @OneToMany(mappedBy = "customer")
    List<Address> addressList;

    @OneToMany(mappedBy = "customer")
    List<CustomerVoucher> customerVoucherList;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Cart cart;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(EntityProperties.ROLE_CUSTOMER));
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

    @PrePersist
    private void createCart() {
        Cart cart = new Cart();
        cart.setCustomer(this);
        this.cart = cart;
    }
}

