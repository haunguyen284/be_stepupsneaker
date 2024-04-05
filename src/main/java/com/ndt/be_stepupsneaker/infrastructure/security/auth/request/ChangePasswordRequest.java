package com.ndt.be_stepupsneaker.infrastructure.security.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangePasswordRequest {

    private String id;

//    @NotBlank(message = "Please enter your current password!")
    private String currentPassword;

    @NotBlank(message = "Please enter your new password!")
    private String newPassword;

    @NotBlank(message = "Please enter your enter the new password")
    private String enterThePassword;
}
