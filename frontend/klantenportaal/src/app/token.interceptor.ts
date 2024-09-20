import { HttpHandlerFn, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { HttpEvent, HttpRequest } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { LoginService } from './services/login.service';

export const TokenInterceptor: HttpInterceptorFn = (req, next) => {

  const loginService = inject(LoginService)
  let accessToken = loginService._accessToken;

  //these urls don't need extra headers
  if (req.url == "http://localhost:8080/api/auth/refresh" || req.url == "http://localhost:8080/api/auth/login") {
    return next(req);
  }

  const addToken = function(req: HttpRequest<any>, accessToken: string): HttpRequest<any> {
    return req.clone({
      setHeaders: {
        Authorization: `Bearer ${accessToken}`
      }
    });
  }

  const handle401Error = async function(req: HttpRequest<any>, next: HttpHandlerFn): Promise<HttpEvent<any>> {
    try {
      if (loginService.refreshTokenExpiration < Date.now()) {
        throw Error("refresh token is expired")
      }
      await loginService.refresh();
      accessToken = loginService._accessToken
      req = addToken(req, accessToken);
      return await firstValueFrom(next(req));
    } catch (error) {
      // If refresh token fails, logout the user
      loginService.logout();
      throw error;
    }
  }

  if (accessToken) {
    req = addToken(req, accessToken);
  }

  return next(req).pipe(
    catchError((error) => {
      if (error.status === 401) {
        return handle401Error(req, next);
      } else {
        throw error;
      }
    })
  );
};