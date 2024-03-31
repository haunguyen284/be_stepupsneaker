    package com.ndt.be_stepupsneaker.infrastructure.security.config;

    import com.ndt.be_stepupsneaker.core.admin.dto.request.employee.AdminEmployeeRequest;
    import com.ndt.be_stepupsneaker.core.admin.repository.customer.AdminCustomerRepository;
    import com.ndt.be_stepupsneaker.core.admin.repository.employee.AdminEmployeeRepository;
    import com.ndt.be_stepupsneaker.entity.customer.Customer;
    import com.ndt.be_stepupsneaker.entity.employee.Employee;
    import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
    import com.ndt.be_stepupsneaker.infrastructure.exception.AccessException;
    import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
    import com.ndt.be_stepupsneaker.util.MessageUtil;
    import io.jsonwebtoken.*;
    import io.jsonwebtoken.io.Decoders;
    import io.jsonwebtoken.security.Keys;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.HttpStatus;
    import org.springframework.security.access.AccessDeniedException;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.stereotype.Service;
    import org.springframework.web.server.ResponseStatusException;

    import java.security.Key;
    import java.security.KeyPair;
    import java.security.PrivateKey;
    import java.security.PublicKey;
    import java.util.Date;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.Optional;
    import java.util.function.Function;

    @RequiredArgsConstructor
    @Service
    public class JwtService {
        private final AdminCustomerRepository adminCustomerRepository;
        private final AdminEmployeeRepository adminEmployeeRepository;
        private final MessageUtil messageUtil;

        public String generateToken(UserDetails userDetails) {
            Optional<Employee> optionalEmployee = adminEmployeeRepository.findByEmail(userDetails.getUsername());
            Optional<Customer> optionalCustomer = adminCustomerRepository.findByEmail(userDetails.getUsername());
            Map<String, Object> extraClaims = new HashMap<>();
            if (optionalCustomer.isPresent()) {
                extraClaims.put("id", optionalCustomer.get().getId());
                extraClaims.put("email", optionalCustomer.get().getEmail());
                extraClaims.put("role", "ROLE_CUSTOMER");
                extraClaims.put("fullName", optionalCustomer.get().getFullName());
            } else if (optionalEmployee.isPresent()) {
                extraClaims.put("id", optionalEmployee.get().getId());
                extraClaims.put("email", optionalEmployee.get().getEmail());
                extraClaims.put("role", optionalEmployee.get().getRole().getName());
                extraClaims.put("fullName", optionalEmployee.get().getFullName());
            } else {
                throw new ResourceNotFoundException(messageUtil.getMessage("user.notfound"));
            }

            return Jwts.builder()
                    .setClaims(extraClaims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1 * 24 * 60 * 60 * 60 * 1000))
//                    .setExpiration(new Date(System.currentTimeMillis() + 1 * 60 * 1000))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        }

        private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
            final Claims claims = extractAllClaims(token);
            return claimsResolvers.apply(claims);
        }

        private Claims extractAllClaims(String token) {
            try {
                return Jwts.parserBuilder()
                        .setSigningKey(getSigningKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
            } catch (ExpiredJwtException ex) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,messageUtil.getMessage("expired.token"));
            }
        }


        public String extractUserName(String token) {
            return extractClaim(token, Claims::getSubject);
        }

        private Key getSigningKey() {
            byte[] key = Decoders.BASE64.decode(EntityProperties.SECRET);
            return Keys.hmacShaKeyFor(key);
        }


        public boolean isTokenValid(String token, UserDetails userDetails) {
            final String username = extractUserName(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        }

        private boolean isTokenExpired(String token) {
            return extractExpiration(token).before(new Date());
        }

        private Date extractExpiration(String token) {
            return extractClaim(token, Claims::getExpiration);
        }


    }
