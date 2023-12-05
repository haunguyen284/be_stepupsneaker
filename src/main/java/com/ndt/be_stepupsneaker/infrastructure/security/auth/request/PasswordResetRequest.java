package com.ndt.be_stepupsneaker.infrastructure.security.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequest {
    @NotBlank(message = "Please enter your new password!")
    private String newPassword;

    @NotBlank(message = "Please enter your confirm password!")
    private String confirmPassword;
}
