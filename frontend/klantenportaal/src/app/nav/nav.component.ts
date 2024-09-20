import { Component } from '@angular/core';
import { RouterModule, RouterOutlet } from '@angular/router';
import { RegisterComponent } from '../register/register.component';
import { LoginComponent } from '../login/login.component';
import { MainComponent } from '../main/main.component';
import { MapComponent } from '../map/map.component';
import { OrderComponent } from '../order/order.component';
import { OrdersComponent } from '../orders/orders.component';
import { CommonModule } from '@angular/common';
import { LoginService } from '../services/login.service';
import ImageLayer from 'ol/layer/Image';

@Component({
  selector: 'app-nav',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterModule, RegisterComponent, LoginComponent, MainComponent, MapComponent, OrderComponent, OrdersComponent, NavComponent],
  templateUrl: './nav.component.html',
  styleUrl: './nav.component.css'
})
export class NavComponent {
  isAdmin: boolean= false;
  constructor(public loginService: LoginService) {}
  ngOnInit() {
    this.isAdmin = this.loginService.isAdmin();
  }
}
