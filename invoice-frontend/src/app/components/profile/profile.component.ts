import { NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { EmployeeService } from '../../services/employee.service';
import { Router } from '@angular/router';
import { AuthService } from '../../auth/auth.service';


@Component({
  selector: 'app-profile',
  imports: [ReactiveFormsModule, NgIf],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {

  profileForm!: FormGroup;
  isEditMode = false;

  constructor(private fb: FormBuilder, private employeeService: EmployeeService, private router: Router, private authService: AuthService) { }
  
  ngOnInit() {
    this.profileForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', Validators.required],
      companyName: ['', Validators.required],
      companyAddress: ['', Validators.required],
      clientName: ['', Validators.required],
      clientEmail: ['', [ Validators.required, Validators.email] ],
      clientAddress: ['', Validators.required],
      rate: ['', Validators.required ]
    })

    this.getMyProfile();
  }

  submitForm() {

    if (this.profileForm.invalid) return;
    
    if (this.isEditMode) {
      this.employeeService.updateMyProfile(this.profileForm.value).subscribe({
        next: (response) => {
          this.router.navigate(['invoices'])
        },
        error: (err) => {
          if (err.status == 401) {
            this.router.navigate(['login'])
          }
        }
      });
    } else {
      this.employeeService.createProfile(this.profileForm.value).subscribe({
        next: (response) => {
          this.router.navigate(['invoices'])
        },
        error: (err) => {
          if (err.status == 401) {
            this.router.navigate(['login'])
          }
        }
      }
      );
    }

  }

  getMyProfile() {
    this.employeeService.getMyProfile().subscribe({
      next: employee => {
        this.profileForm.patchValue(employee);
        this.isEditMode = true;
      },
      error: (err) => {
        if (err.status == 401) {
          this.isEditMode = false;
        }
      }
    })
  }


}
