import { Component } from '@angular/core';
import { NotificationsComponent } from '../notifications/notifications.component';
import { LoginService } from '../services/login.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
  imports: [NotificationsComponent,CommonModule]
})
export class HeaderComponent {
  userName: string = '';
  showDropdown: boolean = false;

  constructor(private loginService: LoginService) {
    this.loginService.userName.subscribe(userName => {
      this.userName = userName; 
    });
  }

  toggleDropdown() {
    this.showDropdown = !this.showDropdown;
  }

  logout() {
    this.loginService.logout(); 
    this.showDropdown = false;
  }
}
