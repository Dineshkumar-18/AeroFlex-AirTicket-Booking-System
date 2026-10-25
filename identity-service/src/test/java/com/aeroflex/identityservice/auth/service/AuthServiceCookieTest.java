package com.aeroflex.identityservice.auth.service;

import com.aeroflex.identityservice.security.jwt.JwtProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.mock.web.MockHttpServletResponse;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceCookieTest {

    private AuthServiceImpl authService;
    private JwtProperties jwtProperties;

    @BeforeEach
    void setUp() {
        jwtProperties = new JwtProperties();
        jwtProperties.setAccessTokenExpiration(900_000L); // 15 min
        jwtProperties.setRefreshTokenExpiration(604_800_000L); // 7 days
        jwtProperties.setAccessTokenCookieName("accessToken");
        jwtProperties.setRefreshTokenCookieName("refreshToken");
        jwtProperties.setCookieSecure(true);
        jwtProperties.setCookieSameSite("None");

        // AuthServiceImpl with dummy dependencies except jwtProperties
        authService = new AuthServiceImpl(
                null, null, null, null, null, null, null, null,
                jwtProperties, null, null, null, null, null, null, null
        );
    }

    @Test
    void shouldBuildSecureHttpOnlyAccessTokenCookie() {
        ResponseCookie cookie = authService.buildAccessTokenCookie("test-access-token");

        assertNotNull(cookie);
        assertEquals("accessToken", cookie.getName());
        assertEquals("test-access-token", cookie.getValue());
        assertTrue(cookie.isHttpOnly(), "AccessToken cookie must be HttpOnly to prevent XSS");
        assertTrue(cookie.isSecure(), "AccessToken cookie must be Secure");
        assertEquals("/", cookie.getPath(), "AccessToken cookie path should be root");
        assertEquals(Duration.ofMillis(900_000L), cookie.getMaxAge());
        assertEquals("None", cookie.getSameSite());
    }

    @Test
    void shouldBuildSecureHttpOnlyRefreshTokenCookieWithRestrictedPath() {
        ResponseCookie cookie = authService.buildRefreshTokenCookie("test-refresh-token");

        assertNotNull(cookie);
        assertEquals("refreshToken", cookie.getName());
        assertEquals("test-refresh-token", cookie.getValue());
        assertTrue(cookie.isHttpOnly(), "RefreshToken cookie must be HttpOnly to prevent XSS");
        assertTrue(cookie.isSecure(), "RefreshToken cookie must be Secure");
        assertEquals("/api/account/auth", cookie.getPath(), "RefreshToken cookie path must be restricted to auth endpoints");
        assertEquals(Duration.ofMillis(604_800_000L), cookie.getMaxAge());
        assertEquals("None", cookie.getSameSite());
    }

    @Test
    void shouldBuildClearCookiesWithZeroMaxAge() {
        ResponseCookie clearAccess = authService.buildClearAccessTokenCookie();
        ResponseCookie clearRefresh = authService.buildClearRefreshTokenCookie();

        assertEquals("", clearAccess.getValue());
        assertEquals(Duration.ZERO, clearAccess.getMaxAge());
        assertTrue(clearAccess.isHttpOnly());
        assertEquals("/", clearAccess.getPath());

        assertEquals("", clearRefresh.getValue());
        assertEquals(Duration.ZERO, clearRefresh.getMaxAge());
        assertTrue(clearRefresh.isHttpOnly());
        assertEquals("/api/account/auth", clearRefresh.getPath());
    }

    @Test
    void shouldSetBothCookiesInHttpServletResponse() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        authService.setAuthCookies(response, "access-val", "refresh-val");

        List<String> setCookies = response.getHeaders(HttpHeaders.SET_COOKIE);
        assertEquals(2, setCookies.size());

        assertTrue(setCookies.stream().anyMatch(c -> c.contains("accessToken=access-val") && c.contains("HttpOnly") && c.contains("Secure")));
        assertTrue(setCookies.stream().anyMatch(c -> c.contains("refreshToken=refresh-val") && c.contains("HttpOnly") && c.contains("Path=/api/account/auth")));
    }

    @Test
    void shouldClearBothCookiesInHttpServletResponse() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        authService.clearAuthCookies(response);

        List<String> setCookies = response.getHeaders(HttpHeaders.SET_COOKIE);
        assertEquals(2, setCookies.size());

        assertTrue(setCookies.stream().anyMatch(c -> c.contains("accessToken=") && c.contains("Max-Age=0")));
        assertTrue(setCookies.stream().anyMatch(c -> c.contains("refreshToken=") && c.contains("Max-Age=0")));
    }
}
