package com.ram.portfolio.auth;

public record AuthResponse(String token, String tokenType, long expiresInSeconds) {}
