package com.example.klantenportaal.service;

import static com.example.klantenportaal.ObjectMother.adminUser;
import static com.example.klantenportaal.ObjectMother.defaultOrder;
import static com.example.klantenportaal.ObjectMother.sailingOrder;
import static com.example.klantenportaal.ObjectMother.user1;
import com.example.klantenportaal.TestAppender;
import com.example.klantenportaal.dto.OrderDTO;
import com.example.klantenportaal.models.User;
import com.example.klantenportaal.services.NotificationService;
import com.example.klantenportaal.services.OrderMonitoringService;
import com.example.klantenportaal.services.OrderService;
import com.example.klantenportaal.services.UserService;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.LoggerFactory;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
public class OrderMonitoringServiceTest {
    @Mock
    private OrderService orderService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private OrderMonitoringService orderMonitoringService;
    private TestAppender testAppender;
    private User user;
    private User adminUser;
    private OrderDTO sailingOrder;
    private OrderDTO defaultOrder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = user1();
        adminUser = adminUser();
        defaultOrder = defaultOrder();
        sailingOrder = sailingOrder();
        Logger logger = (Logger) LoggerFactory.getLogger(OrderMonitoringService.class);
        logger.setLevel(ch.qos.logback.classic.Level.INFO);

        testAppender = new TestAppender();
        testAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        testAppender.start();

        logger.addAppender(testAppender);
    }

    @Test
    public void testGenerateOrderNotificationsWithNoUsers() {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());
        orderMonitoringService.generateOrderNotifications();
        verify(userService, times(1)).getAllUsers();
        verifyNoMoreInteractions(userService, orderService, notificationService);
    }

    @Test
    public void testGenerateOrderNotificationsForAdminUser() {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(adminUser));
        orderMonitoringService.generateOrderNotifications();
        List<ILoggingEvent> logEvents = testAppender.getEvents();
        assertEquals(2, logEvents.size());
        assertEquals("Admin is not allowed notifications: " + adminUser.getUsername(), logEvents.get(1).getFormattedMessage());
    }

    @Test
    public void testGenerateOrderNotificationsForValidUser() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(user));
        when(orderService.getAllOrders(user)).thenReturn(Collections.singletonList(sailingOrder));
        when(notificationService.notificationExists(sailingOrder.referenceNumber())).thenReturn(false);

        orderMonitoringService.generateOrderNotifications();

        verify(userService, times(1)).getAllUsers();
        verify(orderService, times(1)).getAllOrders(user);
        verify(notificationService, times(1)).notificationExists(defaultOrder.referenceNumber());
        verify(notificationService, times(1)).saveNotification(any());
    }
}
