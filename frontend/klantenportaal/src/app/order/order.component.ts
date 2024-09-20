import { Component, ElementRef, ViewChild } from '@angular/core';
import { IOrderDetail } from '../interfaces/iorder-detail';
import { DomSanitizer, SafeResourceUrl, SafeUrl } from '@angular/platform-browser';
import { OrdersService } from '../services/orders.service';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';



@Component({
  selector: 'app-order',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './order.component.html',
  styleUrl: './order.component.css'
})
export class OrderComponent {

  constructor(private ordersService: OrdersService,private sanitizer: DomSanitizer, private route: ActivatedRoute) {}

  iframeSrc: SafeUrl | null = null;
  orderDetail: IOrderDetail | null = null;

  ngOnInit() {
    // Subscribe to route parameter changes
    this.route.params.subscribe(params => {
      const companyCode = params["company"];
      const id = params['id'];
      this.getOrderDetails(companyCode, id);
    });
  }

  @ViewChild('vesselMapIframe', { static: true }) iframe!: ElementRef<HTMLIFrameElement>;

  updateIframeSrc(imo: string): void {
    const src = `https://www.vesselfinder.com/aismap?imo=${imo}&width=800&height=500&names=true`;
    this.iframeSrc = this.sanitizer.bypassSecurityTrustResourceUrl(src);
  }
  
  async getOrderDetails(companyCode: string, ref: string) {
    try {
      this.orderDetail = await this.ordersService.getOrderDetails(companyCode, ref);
      if (this.orderDetail.shipIMO != null && this.orderDetail.state != "CLOSED") {
        this.updateIframeSrc(this.orderDetail.shipIMO);
      }else{
        this.iframeSrc = null
      }

    } catch (error) {
      console.error('Error fetching order details:', error);
    } 
  }

  getTotalWeight(){
    let totalWeight = 0;
    if (this.orderDetail != null) {
      this.orderDetail.products.forEach((product) =>{
        totalWeight += product.weight;
      });
    }
    return totalWeight;
  }
}
