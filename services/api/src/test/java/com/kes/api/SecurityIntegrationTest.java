package com.kes.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kes.api.dto.LoginRequest;
import com.kes.api.entity.Role;
import com.kes.api.entity.User;
import com.kes.api.repository.RoleRepository;
import com.kes.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role adminRole = new Role(null, "ROLE_ADMIN");
        Role userRole = new Role(null, "ROLE_USER");
        roleRepository.save(adminRole);
        roleRepository.save(userRole);

        User admin = new User(null, "admin", passwordEncoder.encode("admin123"), Set.of(adminRole));
        User user = new User(null, "user", passwordEncoder.encode("user123"), Set.of(userRole));
        userRepository.save(admin);
        userRepository.save(user);
    }

    @Test
    void login_WithValidCredentials_ShouldReturnJwt() throws Exception {
        LoginRequest loginRequest = new LoginRequest("admin", "admin123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.roles").value("ROLE_ADMIN"));
    }

    @Test
    void login_WithInvalidCredentials_ShouldReturn401() throws Exception {
        LoginRequest loginRequest = new LoginRequest("admin", "wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void accessAdminEndpoint_AsAdmin_ShouldSucceed() throws Exception {
        String token = getJwtToken("admin", "admin123");

        mockMvc.perform(get("/api/audit-logs")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void accessAdminEndpoint_AsUser_ShouldReturn403() throws Exception {
        String token = getJwtToken("user", "user123");

        mockMvc.perform(get("/api/audit-logs")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void accessAdminEndpoint_WithoutToken_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/audit-logs"))
                .andExpect(status().isUnauthorized());
    }

    private String getJwtToken(String username, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest(username, password);
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn().getResponse().getContentAsString();
        
        return objectMapper.readTree(response).get("token").asText();
    }
}
