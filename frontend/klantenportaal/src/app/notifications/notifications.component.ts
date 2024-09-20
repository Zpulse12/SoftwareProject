import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService, Notification } from '../services/notification.service'; 

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css'] ,
  providers: [NotificationService]
})
export class NotificationsComponent  {
  showNotifications = false;
  notifications: Notification[] = [];

  constructor(private notificationService: NotificationService) { }

  ngOnInit(): void {
    this.fetchNotifications();
  }
  deleteNotification(id: number) {
    this.notificationService.deleteNotification(id).subscribe({
      next: (res) => {
        console.log('Notification deleted successfully!');
        this.notifications = this.notifications.filter(notification => notification.id !== id);
      },
      error: (e) => console.error('Error deleting notification', e)
    });
  }

  toggleNotifications(): void {
    this.showNotifications = !this.showNotifications;
  }

  fetchNotifications(): void {
    this.notificationService.getNotifications().subscribe({
      next: (notifications) => {
        this.notifications = notifications;
        console.log('Notifications fetched:', notifications);
      },
      error: (error) => {
        console.error('Error fetching notifications:', error);
      }
    });
  }

}
