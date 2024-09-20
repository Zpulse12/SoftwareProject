package com.example.klantenportaal.services;

import com.example.klantenportaal.dto.OrderDTO;
import com.example.klantenportaal.models.Notification;
import com.example.klantenportaal.models.User;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
/**
 * Service voor het monitoren van orders en het genereren van notificaties.
*/
@Service
public class OrderMonitoringService {
    private static final Logger logger = LoggerFactory.getLogger(OrderMonitoringService.class);

    private final OrderService orderService;
    private final NotificationService notificationService;
    private final UserService userService;
    /**
     * Construeert een OrderMonitoringService met de opgegeven services.
     *
     * @param orderService de service voor het beheren van orders
     * @param notificationService de service voor het beheren van notificaties
     * @param userService de service voor het beheren van gebruikers
    */
    public OrderMonitoringService(OrderService orderService, NotificationService notificationService,
            UserService userService) {
        this.orderService = orderService;
        this.notificationService = notificationService;
        this.userService = userService;
        logger.info("OrderMonitoringService is initialized");
    }
    /**
     * Genereert notificaties voor orders die in de status "SAILING" zijn en nog niet eerder een notificatie hebben ontvangen. Er worden maximaal 2 notificaties per gebruiker gegenereerd.
    */
    @Scheduled(fixedRate = 86400000)
    public void generateOrderNotifications() {
        List<User> users = userService.getAllUsers();
        logger.info("Running scheduled task for {} users", users.size());

        for (User user : users) {
            try {
                if ("Steelduxx".equals(user.getCompanyCode()) || user.getCompanyCode() == null) {
                    logger.error("Admin is not allowed notifications: {}", user.getUsername());
                    continue;
                }

                List<OrderDTO> orders = orderService.getAllOrders(user);
                logger.info("Total orders fetched for user {}: {}", user.getUsername(), orders.size());
                int notificationsCreated = 0;

                for (OrderDTO order : orders) {
                    if ("SAILING".equals(order.state()) && notificationsCreated < 2) {
                        Notification notification = new Notification(
                                order.shipName(),
                                order.state(),
                                order.eta(),
                                order.portOfDestinationName(),
                                order.referenceNumber(),
                                user.getCompanyCode());

                        if (!notificationService.notificationExists(notification.getReferenceNumber())) {
                            notificationService.saveNotification(notificationService.convertToDTO(notification));
                            logger.info("Notification generated for order: {}", order.referenceNumber());
                            notificationsCreated++;

                            if (notificationsCreated >= 2) {
                                logger.info("Maximum number of notifications reached for this period for user {}.",
                                        user.getUsername());
                                break;
                            }
                        } else {
                            logger.info("Notification already exists for order: {}", order.referenceNumber());
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Error generating order notifications for user {}: {}", user.getUsername(),
                        e.getMessage());
            }
        }
    }
}
