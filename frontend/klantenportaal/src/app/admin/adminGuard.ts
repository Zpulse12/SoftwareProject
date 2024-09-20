import { CanActivate, Router, UrlTree } from '@angular/router';
import { LoginService } from '../services/login.service';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {
  constructor(private loginService: LoginService, private router: Router) {}

  canActivate(): Observable<boolean | UrlTree> {
    return this.loginService.userRole.pipe(
      map((role: string) => {
        if (role === 'admin') {
          return true;
        } else {
          return this.router.createUrlTree(['home']);
        }
      })
    );
  }
}



