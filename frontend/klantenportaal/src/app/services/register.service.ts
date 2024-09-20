import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  //private backendUrl = 'https://sw02.devops-ap.be'; // Replace with your actual backend URL
  private backendUrl = 'http://localhost:8080/api/auth/signup';

  constructor(private http: HttpClient) {
  }

async registerUser(userData: any): Promise<any> {
  try {
    const result = await this.http.post(this.backendUrl, userData).toPromise();
    return result;
  } catch (error) {
    throw error;
}
}
}

