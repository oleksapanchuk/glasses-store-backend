package com.oleksa.ecommerce.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String extractUserName(String token);

    String generateToken(UserDetails userDetails, boolean isRefresh);

    boolean isTokenValid(String token, UserDetails userDetails);


}
