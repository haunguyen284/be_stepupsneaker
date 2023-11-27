package com.ndt.be_stepupsneaker.infrastructure.security.auth.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Builder
public class RegisterRequest {

    private String email;

    private String fullName;

    private String address;

    private String gender;

    private String passWord;

    private String role;

}
