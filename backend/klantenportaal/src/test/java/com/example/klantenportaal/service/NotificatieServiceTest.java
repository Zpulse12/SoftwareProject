package com.example.klantenportaal.service;

import static com.example.klantenportaal.ObjectMother.notification1;
import static com.example.klantenportaal.ObjectMother.notifications;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.klantenportaal.dto.NotificationDTO;
import com.example.klantenportaal.jpa.NotificationRepository;
import com.example.klantenportaal.models.Notification;
import com.example.klantenportaal.services.NotificationService;

public class NotificatieServiceTest {
    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private Notification notification;
    private NotificationDTO notificationDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notification = notifications();
        notification.setId(1L);
        notificationDTO = notification1();
    }

    @Test
    void testGetAllNotifications() {
        when(notificationRepository.findAll()).thenReturn(List.of(notification));
        List<NotificationDTO> result = notificationService.getAllNotifications();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(notificationDTO, result.get(0));
    }

    @Test
    void testGetNotificationsByCompanyCode() {
        String companyCode = "Comp456";
        when(notificationRepository.findByCompanyCode(companyCode)).thenReturn(List.of(notification));
        List<NotificationDTO> result = notificationService.getNotificationsByCompanyCode(companyCode);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(notificationDTO, result.get(0));
    }

    @Test
    void testNotificationExists() {
        String referenceNumber = "Ref123";
        when(notificationRepository.findByReferenceNumber(referenceNumber)).thenReturn(Optional.of(notification));
        boolean exists = notificationService.notificationExists(referenceNumber);
        assertTrue(exists);
    }

    @Test
    void testSaveNotification() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        NotificationDTO result = notificationService.saveNotification(notificationDTO);
        assertNotNull(result);
        assertEquals(notificationDTO, result);
    }

    @Test
    void testDeleteNotification() {
        Long id = 1L;
        when(notificationRepository.findById(id)).thenReturn(Optional.of(notification));
        NotificationDTO result = notificationService.deleteNotification(id);
        assertNotNull(result);
        assertEquals(notificationDTO, result);
        verify(notificationRepository, times(1)).delete(notification);
    }

    @Test
    void testGetNotificationById() {
        Long id = 1L;
        when(notificationRepository.findById(id)).thenReturn(Optional.of(notification));
        NotificationDTO result = notificationService.getNotificationById(id);
        assertNotNull(result);
        assertEquals(notificationDTO, result);
    }

    @Test
    void testGetNotificationById_NotFound() {
        Long id = 1L;
        when(notificationRepository.findById(id)).thenReturn(Optional.empty());
        NotificationDTO result = notificationService.getNotificationById(id);
        assertNull(result);
    }
}
