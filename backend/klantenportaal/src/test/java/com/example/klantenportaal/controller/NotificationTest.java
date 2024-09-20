package com.example.klantenportaal.controller;

import com.example.klantenportaal.controllers.NotificationController;
import com.example.klantenportaal.dto.NotificationDTO;
import com.example.klantenportaal.models.User;
import com.example.klantenportaal.seeders.UserSeeder;
import com.example.klantenportaal.services.JwtService;
import com.example.klantenportaal.services.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import java.util.List;

import static com.example.klantenportaal.ObjectMother.adminUser;
import static com.example.klantenportaal.ObjectMother.notification1;
import static com.example.klantenportaal.ObjectMother.notification2;
import static com.example.klantenportaal.ObjectMother.notification3;
import static com.example.klantenportaal.ObjectMother.setAuthentication;
import static com.example.klantenportaal.ObjectMother.user1;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private NotificationService notificationService;
    @MockBean
    private UserSeeder userSeeder;
    @MockBean
    private JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllNotifications_ShouldReturnNotifications() throws Exception {
        User user = adminUser();
        setAuthentication(user);
        List<NotificationDTO> allNotifications = Arrays.asList(notification1(), notification2());

        given(notificationService.getAllNotifications()).willReturn(allNotifications);

        mockMvc.perform(get("/api/notifications/All"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].shipName").value(notification1().shipName()));
    }

    @Test
    void createNotification_ShouldSaveNotification() throws Exception {
        User user = adminUser();
        setAuthentication(user);

        NotificationDTO mockNotification = notification3();
        given(notificationService.saveNotification(notification3())).willReturn(mockNotification);

        String jsonRequest = objectMapper.writeValueAsString(mockNotification);
        mockMvc.perform(post("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shipName").value(mockNotification.shipName()));
    }

    @Test
    void getNotificationsForCurrentUser_ShouldReturnNotifications() throws Exception {
        User user = user1();
        setAuthentication(user);
        List<NotificationDTO> allNotifications = Arrays.asList(notification1(), notification2());
        given(notificationService.getNotificationsByCompanyCode(user.getCompanyCode()))
                .willReturn(allNotifications);

        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].shipName").value(notification1().shipName()));
    }

}
