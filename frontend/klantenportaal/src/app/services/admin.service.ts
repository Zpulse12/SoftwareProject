import { Injectable } from '@angular/core';
import { Observable, catchError, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private clientsUrl = 'http://localhost:8080/api/registraties';  // URL to the JSON file
  constructor(private http: HttpClient) {  }

  getClients(): Observable<any> {
    return this.http.get(this.clientsUrl);
  }
  updateStatus(id: number, status: string): Observable<any> {
    return this.http.put(`${this.clientsUrl}/${id}/status`, status);
  }
  updateClient(client: any): Observable<any> {
    return this.http.put(`${this.clientsUrl}/${client.id}/details`, client);
  }
  
deleteClient(id: number): Observable<any> {
  return this.http.delete(`${this.clientsUrl}/${id}`);
}

}
