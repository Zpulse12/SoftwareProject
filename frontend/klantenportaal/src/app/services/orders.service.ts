import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { IOrder } from '../interfaces/iorder';
import { IOrderDetail } from '../interfaces/iorder-detail';

@Injectable({
  providedIn: 'root',
})
export class OrdersService {

  constructor(private http: HttpClient) {
  }

  async getAllOrders(): Promise<IOrder[]> {
    return lastValueFrom(this.http.get<IOrder[]>('http://localhost:8080/api/orders'));
  }

  async getOrderDetails(companyCode: string, ref: string): Promise<IOrderDetail> {
    return lastValueFrom(this.http.get<IOrderDetail>(`http://localhost:8080/api/orders/${companyCode}/${ref}`));
  }
  createOrder(orderData: IOrderDetail): Observable<string> {
    return this.http.post<string>('http://localhost:8080/api/orders', orderData, { responseType: 'text' as 'json' });
  }
}
