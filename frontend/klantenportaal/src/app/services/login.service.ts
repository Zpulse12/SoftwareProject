import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, lastValueFrom } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private userRoleSource = new BehaviorSubject<string>('');
  private userNameSource = new BehaviorSubject<string>('');
  userRole = this.userRoleSource.asObservable();
  userName = this.userNameSource.asObservable();


  isLoggedIn: boolean;
  _accessToken: string;
  accessTokenExpiration: number;
  private _refreshToken: string;
  refreshTokenExpiration: number;
  isAdmin(): boolean {
    return this.userRoleSource.value === 'admin';
  }
  constructor(private http: HttpClient, private router: Router) {
    if (typeof localStorage !== 'undefined') {
      // Initialize properties if localStorage is available
      this._accessToken = localStorage.getItem('accessToken') || '';
      this._refreshToken = localStorage.getItem('refreshToken') || '';
      this.accessTokenExpiration = localStorage.getItem('accessTokenExpiration') ? +localStorage.getItem('accessTokenExpiration')! : 0;
      this.refreshTokenExpiration = localStorage.getItem('refreshTokenExpiration') ? +localStorage.getItem('refreshTokenExpiration')! : 0;
      this.userRoleSource.next(localStorage.getItem('userRole') || '');
      this.userNameSource.next(localStorage.getItem('userName') || '');
      console.log(localStorage.getItem('isLoggedIn'));
      console.log(localStorage.getItem('userRole'));
      this.isLoggedIn = (this._accessToken != '')
    } else {
      this._accessToken = '';
      this._refreshToken = '';
      this.accessTokenExpiration = 0;
      this.refreshTokenExpiration = 0;
      this.isLoggedIn = false;
    }

  }


  private getToken(email: string, password: string): Promise<{token:string, expiresIn:number, refreshToken:string, refreshTokenExpiresIn:number, role:string, userName:string}> {
    const credentials = { email, password };
    return lastValueFrom(this.http.post<any>('http://localhost:8080/api/auth/login', credentials));
  }

  private getRefreshToken(token: string): Promise<{token:string, expiresIn:number, refreshToken:string, refreshTokenExpiresIn:number}> {
    const credentials = { token };
    return lastValueFrom(this.http.post<any>('http://localhost:8080/api/auth/refresh', credentials));
  }

  async login(email: string, password: string) {
    try {
      const tokenObject = await this.getToken(email, password);
      this._accessToken = tokenObject.token;
      this.accessTokenExpiration = (Date.now() + tokenObject.expiresIn);
      this._refreshToken = tokenObject.refreshToken;
      this.refreshTokenExpiration = (Date.now() + tokenObject.refreshTokenExpiresIn);
      this.isLoggedIn = true;
      this.userRoleSource.next(tokenObject.role)
      this.userNameSource.next(tokenObject.userName)
      localStorage.setItem('isLoggedIn', 'true');
      localStorage.setItem('userRole', this.userRoleSource.value);
      localStorage.setItem('userName', this.userNameSource.value);
      localStorage.setItem('accessToken', this._accessToken);
      localStorage.setItem('accessTokenExpiration', this.accessTokenExpiration.toString());
      localStorage.setItem('refreshToken', this._refreshToken);
      localStorage.setItem('refreshTokenExpiration', this.refreshTokenExpiration.toString());
      this.router.navigate(['home/orders']);
    } catch (error) {
      console.error('Login error:', error);
      throw error;
    }
  }
  logout() {
    this._accessToken = "";
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('userRole');
    localStorage.removeItem('userName');
    this.userRoleSource.next('');
    this.userNameSource.next('');
    this.router.navigate(['/login']);
  }
  
  async refresh(): Promise<string> {
    try {
      const tokenObject = await this.getRefreshToken(this._refreshToken);
      this._accessToken = tokenObject.token;
      this.accessTokenExpiration = (Date.now() + (tokenObject.expiresIn-2000));
      this._refreshToken = tokenObject.refreshToken;
      this.refreshTokenExpiration = (Date.now() + (tokenObject.refreshTokenExpiresIn-2000)); // - 2 seconds to give time for refresh request
      this.isLoggedIn = true;

      localStorage.setItem('accessToken', this._accessToken);
      localStorage.setItem('accessTokenExpiration', this.accessTokenExpiration.toString());
      localStorage.setItem('refreshToken', this._refreshToken);
      localStorage.setItem('refreshTokenExpiration', this.refreshTokenExpiration.toString());

      return tokenObject.token;
    } catch (error) {
      console.error(error);
      return "refresh token error";
    }
  }
}
