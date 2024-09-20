import { Component } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { MapComponent } from '../map/map.component';
import { NavComponent } from '../nav/nav.component';
import { Router, RouterOutlet } from '@angular/router';
import { LoginService } from '../services/login.service';
import { NotificationsComponent } from '../notifications/notifications.component';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [HeaderComponent, MapComponent, NavComponent, RouterOutlet,NotificationsComponent],
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css'],
})
export class MainComponent {
  constructor(public loginService: LoginService, router: Router) {
    if (!loginService.isLoggedIn) {
      router.navigate(['login']);
    }
  }
}
