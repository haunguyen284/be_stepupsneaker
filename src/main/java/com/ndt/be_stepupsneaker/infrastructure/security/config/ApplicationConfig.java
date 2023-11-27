package com.ndt.be_stepupsneaker.infrastructure.security.config;

import com.ndt.be_stepupsneaker.core.admin.repository.employee.AdminEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//  @RequiredArgsConstructor được sử dụng để tạo một constructor với tham số cho EmployeeRepository.
//  Điều này giúp tự động inject EmployeeRepository vào lớp khi Spring IoC container khởi tạo bean ApplicationConfig.
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final AdminEmployeeRepository adminEmployeeRepository;

    // Tìm kiếm thông tin người dùng dựa trên email
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> adminEmployeeRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Cấu hình nhiệm vụ là xác minh phương thức đăng nhập của người dùng
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    // Quản lí quá trình xác thực
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // Mã hóa mật khẩu
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }
}
