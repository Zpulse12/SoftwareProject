import { Component } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { RegisterService } from '../services/register.service';
import { CountryListComponent, ICountry } from 'ngx-countries-dropdown';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, CountryListComponent]
})
export class RegisterComponent {
  registerForm!: FormGroup;
  selectedCountry: ICountry = {};

  constructor(
    private fb: FormBuilder,
    private registerService: RegisterService,
    private router: Router
  ) {
    this.initForm();
  }

  private initForm(): void {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      fullName: ['', Validators.required],
      password: ['', [Validators.required, this.passwordFormatValidator]],
      passwordRepeat: ['', Validators.required],
      vat: [''],
      additionalAdress: [''],
      companyName: ['', Validators.required],
      phoneNumber: ['', [Validators.required, Validators.pattern(/^[0-9]+$/)]],
      streetName: ['', Validators.required],
      houseNumber: ['', Validators.required],
      city: ['', Validators.required],
      country: ['', Validators.required],
    });
  }
  logFormErrors() {
    for (const key in this.registerForm.controls) {
      const control = this.registerForm.get(key);
      if (control && control.errors) {
        console.log(key, control.errors);
      }
    }
  }
  
  passwordFormatValidator(control: AbstractControl): { [key: string]: any } | null {
    const password = control.value;
    const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    if (!passwordRegex.test(password)) {
      return { 'passwordInvalid': true };
    }
    return null;
  }

  submit(): void {
    if (this.registerForm.invalid) {
      console.error('Form is not valid');
      this.registerForm.markAllAsTouched();
      this.logFormErrors(); 
      return;
    }

    const passwordsMatch = this.registerForm.value.password === this.registerForm.value.passwordRepeat;
    if (!passwordsMatch) {
      console.error("Passwords don't match");
      return;
    }

    const userData = {
      ...this.registerForm.value,
      country: this.selectedCountry.name
    };
    delete userData.passwordRepeat;

    this.registerService.registerUser(userData)
      .then(response => {
        console.log('Registration successful:', response);
        this.router.navigate(['login']);
      })
      .catch(error => {
        console.error('Registration failed:', error);
      });
  }

  onCountryChange(country: ICountry): void {
    this.selectedCountry = country;
    this.registerForm.get('country')?.setValue(country.name); 
  }
  
}
