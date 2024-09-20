import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface Notification {
  shipName: string;
  departureStatus: string;
  departureTime: string;
  id: number;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private notiUrl = 'http://localhost:8080/api/notifications';
  private noti = this.notiUrl+'/user';

  constructor(private http: HttpClient) {  }

  getNotifications(): Observable<Notification[]> {
    return this.http.get<Notification[]>(this.noti);
}

  deleteNotification(id: number): Observable<any> {
    return this.http.delete(`${this.notiUrl}/${id}`);
  }

}