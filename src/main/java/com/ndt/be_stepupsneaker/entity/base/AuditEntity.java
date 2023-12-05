package com.ndt.be_stepupsneaker.entity.base;

import com.ndt.be_stepupsneaker.core.admin.repository.employee.AdminEmployeeRepository;
import com.ndt.be_stepupsneaker.infrastructure.listener.AuditEntityListener;
import com.ndt.be_stepupsneaker.infrastructure.security.session.MySessionInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditEntityListener.class)
@RequiredArgsConstructor
public abstract class AuditEntity {
    @Column(name = "created_at", updatable = false)
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

//    @PrePersist
//    protected void onCreate() {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (userDetails != null) {
//            this.createdBy = userDetails.getUsername();
//        }
//    }
//
//    @PreUpdate
//    protected void onUpdate() {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (userDetails != null) {
//            this.updatedBy = userDetails.getUsername();
//        }
//
//    }
}