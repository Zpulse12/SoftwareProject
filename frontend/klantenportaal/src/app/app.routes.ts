import { Routes } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { MainComponent } from './main/main.component';
import { MapComponent } from './map/map.component';
import { OrderComponent } from './order/order.component';
import { OrdersComponent } from './orders/orders.component';
import { AdminComponent } from './admin/admin.component';
import { AdminGuard } from './admin/adminGuard';
import { MakeOrderComponent } from './MakeOrder/MakeOrder.component';

export const routes: Routes = [
    { path: 'register', component: RegisterComponent, title: "Steelduxx Customer Portal" },
    { path: 'login', component: LoginComponent, title: "Steelduxx Customer Portal" },
    { path: 'home', redirectTo: 'home/orders', pathMatch: 'full', title: "Steelduxx Customer Portal" },
    { path: 'home', component: MainComponent, children: [
        { path: 'map', component: MapComponent},
        {path: 'neworder', component: MakeOrderComponent},
        {path: 'admin', component: AdminComponent, canActivate: [AdminGuard]},
        { path: 'orders', component: OrdersComponent, children: [
            { path: 'order/:id', component: OrderComponent },
            { path: 'order/:company/:id', component: OrderComponent }
        ], title: "Steelduxx Customer Portal" }
    ] },
    { path: '', redirectTo: 'home/orders', pathMatch: 'full' },
    { path: '**', redirectTo: 'home/orders' }

];
