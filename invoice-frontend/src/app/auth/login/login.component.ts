import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { EmployeeService } from '../../services/employee.service';
import { async } from 'rxjs';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  loginForm!: FormGroup;
  isSigningIn: boolean = false;
  errorMessage: string = '';

  constructor(private authService: AuthService, private fb: FormBuilder, private router: Router, private employeeService: EmployeeService) { }
  
  ngOnInit() {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: [ '', Validators.required ]
    })
  }

  onSubmit() {
    if (this.loginForm.invalid) return;

    const email = this.loginForm.get('email')?.value;
    const password = this.loginForm.get('password')?.value;

    this.isSigningIn = true;

    this.authService.login(email, password).subscribe({
      next: async (response) => {

        try {
          await response.user.getIdToken();

          this.employeeService.getMyProfile().subscribe({
            next: () => {
              
              this.router.navigate(['invoices']);
              this.isSigningIn = false;
            },
            error: (err) => {
              this.isSigningIn = false;
              if (err.status == 404) {
                this.router.navigate(['profile']);
                return;
              }
              if (err.status == 0) {
                this.errorMessage = 'Backend server is not reachable'
                return;
              }
              if (err.status === 401 || err.status === 403) {
                this.errorMessage = 'You are not authorized. Please login again.';
                return;
              }
              this.errorMessage = 'Something went wrong while loading your profile.';
            }
          });
        } catch (e) {
          this.isSigningIn = false;
          this.errorMessage = 'Login succeeded but token could not be retrieved.';
        }
      },
      error: (err) => {
        this.isSigningIn = false;
        if (err.code === 'auth/network-request-failed') {
          this.errorMessage = 'No internet connection. Please check your network.';
          return;
        } else if (err.code === 'auth/invalid-credential') {
          this.errorMessage = 'Invalid username or password.';
          return;
        } else {
          this.errorMessage = 'Something went wrong, please try again.';
          return;
        }
      }
    });
  }

}
