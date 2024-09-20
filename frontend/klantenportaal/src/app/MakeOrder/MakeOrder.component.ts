import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, FormsModule, Validators } from '@angular/forms';
import { OrdersService } from '../services/orders.service';
import { ReactiveFormsModule } from '@angular/forms';
import { lastValueFrom } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-MakeOrder',
  templateUrl: './MakeOrder.component.html',
  standalone: true,
  styleUrls: ['./MakeOrder.component.css'],
  imports: [FormsModule,CommonModule,ReactiveFormsModule]
})
export class MakeOrderComponent{
  orderForm!: FormGroup;
  isLoading = false;
  isPopupVisible = false;
  ref: string = "";
  productVisibility: boolean[] = []; 

  constructor(private fb: FormBuilder, private orderService: OrdersService, private router: Router) {
    this.initForm();
  }

  ngOnInit(): void {
    this.orderForm.get('cargoType')?.valueChanges.subscribe(value => {
      this.updateProductValidators();
    });
  }

  initForm() {
    this.orderForm = this.fb.group({
      customerReferenceNumber: ['', Validators.required],
      transportType: ['', Validators.required],
      portCode: ['', Validators.required],
      cargoType: ['', Validators.required],
      products: this.fb.array([])
    });
  }

  get products(): FormArray {
    return this.orderForm.get('products') as FormArray;
    this.productVisibility.push(true); 
  }
  toggleProduct(index: number): void {
    this.productVisibility[index] = !this.productVisibility[index];
  }
  addProduct(): void {
    this.products.push(this.createProductGroup());
  }

  createProductGroup(): FormGroup {
    return this.fb.group({
      hsCode: ['', Validators.required],
      name: ['', Validators.required],
      quantity: ['', [Validators.required, Validators.min(1)]],
      weight: ['', Validators.required],
      containerNumber: [''],
      containerType: [''],
      containerSize: ['']
    });
  }

  removeProduct(index: number): void {
    this.products.removeAt(index);
  }

  async submitOrder() {
    if (!this.orderForm.valid) {
      console.error('Form is not valid');
      return;
    }
    this.isLoading = true;
    let formData = this.orderForm.value;
    if (formData.cargoType === 'BULK') {
      formData.products = formData.products.map((product: any) => ({
        ...product,
        containerNumber: null,
        containerType: null,
        containerSize: null
      }));
    }

    try {
      const response = await lastValueFrom(this.orderService.createOrder(formData));
      const parsedResponse = typeof response === 'string' ? JSON.parse(response) : response;
      this.ref = parsedResponse.referenceNumber;
      this.isPopupVisible = true;
      this.orderForm.reset();
    } catch (error) {
      console.error('Error creating order:', error);
    } finally {
      this.isLoading = false;
    }
  }

  viewOrder() {
    this.router.navigate(['/home/orders/order', this.ref]);
  }

  dismissPopup() {
    this.isPopupVisible = false;
  }

  updateProductValidators(): void {
    const cargoType = this.orderForm.get('cargoType')?.value;
    this.products.controls.forEach(control => {
      const validators = cargoType === 'BULK' ? [] : [Validators.required];
      ['containerNumber', 'containerType', 'containerSize'].forEach(field => {
        control.get(field)?.setValidators(validators);
        control.get(field)?.updateValueAndValidity();
      });
    });
    this.orderForm.updateValueAndValidity(); // Optimaliseer door één keer de hele form bij te werken
  }
}
  
  
  
  
