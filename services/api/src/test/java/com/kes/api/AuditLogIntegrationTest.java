package com.kes.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kes.api.dto.LoginRequest;
import com.kes.api.dto.PageCreateDto;
import com.kes.api.entity.Role;
import com.kes.api.entity.User;
import com.kes.api.repository.AuditLogRepository;
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
public class AuditLogIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken;

    @BeforeEach
    void setUp() throws Exception {
        auditLogRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role adminRole = new Role(null, "ROLE_ADMIN");
        roleRepository.save(adminRole);

        User admin = new User(null, "admin", passwordEncoder.encode("admin123"), Set.of(adminRole));
        userRepository.save(admin);

        adminToken = getJwtToken("admin", "admin123");
    }

    @Test
    void auditLog_ShouldRecordPageCreation() throws Exception {
        PageCreateDto createDto = PageCreateDto.builder().title("Audit Test Page").build();

        // Create a page
        mockMvc.perform(post("/api/pages")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated());

        // Verify audit log
        mockMvc.perform(get("/api/audit-logs")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].action").value("CREATE"))
                .andExpect(jsonPath("$[0].resourceType").value("PAGE"))
                .andExpect(jsonPath("$[0].username").value("admin"))
                .andExpect(jsonPath("$[0].details").value("Title: Audit Test Page"));
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
