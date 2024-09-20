import { Component } from '@angular/core';
import { RouterModule, RouterOutlet } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { MainComponent } from './main/main.component';
import { MapComponent } from './map/map.component';
import { OrderComponent } from './order/order.component';
import { OrdersComponent } from './orders/orders.component';
import { NavComponent } from './nav/nav.component';
import { HeaderComponent } from './header/header.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { RegisterService } from './services/register.service';
import { AdminComponent } from './admin/admin.component';
import { NgxCountriesDropdownModule } from 'ngx-countries-dropdown';
import { OrdersService } from './services/orders.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterModule, RegisterComponent, LoginComponent, MainComponent, 
    MapComponent, OrderComponent, OrdersComponent, NavComponent, HeaderComponent, 
    FormsModule, ReactiveFormsModule, AdminComponent, NgxCountriesDropdownModule],
  providers: [RegisterService, OrdersService,],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Steelduxx Customer Portal';
}
