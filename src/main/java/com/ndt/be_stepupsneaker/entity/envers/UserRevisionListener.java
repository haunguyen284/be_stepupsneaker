package com.ndt.be_stepupsneaker.entity.envers;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class UserRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        AuditEnversInfo customRevisionEntity = (AuditEnversInfo) revisionEntity;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            customRevisionEntity.setUsername(userDetails.getUsername());
        } else {
            customRevisionEntity.setUsername("unknown");
        }
    }
}
