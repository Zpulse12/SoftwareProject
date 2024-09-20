import { Component, NgModule } from '@angular/core';
import { LoginService } from '../services/login.service';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  loginError: string = '';

  constructor(private loginService: LoginService,private router: Router) { }

  async login(): Promise<void> {
    this.loginError = '';
    try {
      await this.loginService.login(this.email, this.password);
    } catch (error) {
      this.loginError = 'Incorrect email and/or password';
    }
  }
  register() {
    try {
      this.router.navigate(['register']);
    } catch (error) {
      console.error('Navigation failed:', error);
    }
  }
}