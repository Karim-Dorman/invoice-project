import { Component } from '@angular/core';
import { AuthService } from '../../auth/auth.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Value } from '@angular/fire/compat/remote-config';
import { Router } from '@angular/router';

@Component({
  selector: 'app-change-password',
  imports: [ReactiveFormsModule],
  templateUrl: './change-password.component.html',
  styleUrl: './change-password.component.css'
})
export class ChangePasswordComponent {

  passwordForm!: FormGroup;
  errorMessage: string = '';

  constructor(private authService: AuthService, private fb: FormBuilder, private router: Router) { }

  ngOnInit() {
    this.passwordForm = this.fb.group({
      currentPassword: ['', Validators.required],
      newPassword: ['', Validators.required],
      confirmPassword: ['', Validators.required ]
    })
  }
  
  async onSubmit() {
    const currentPassword = this.passwordForm.get('currentPassword')?.value;
    const newPassword = this.passwordForm.get('newPassword')?.value;
    const confirmPassword = this.passwordForm.get('confirmPassword')?.value;

    if (this.passwordForm.invalid) {
      return;
    }

    if (newPassword !== confirmPassword) {
      this.errorMessage = 'New password and confirm password do not match.';
      return;
    }

    if (!newPassword || newPassword.length < 6) {
      this.errorMessage = "Password must be at least 6 characters";
      return;
    }

    try {
      await this.authService.changePassword(currentPassword, newPassword);
      await this.authService.logout()
      this.router.navigate(['/login']);
    } catch (error) {
      this.errorMessage = 'Something went wront, try agin please'
      console.log(error);
    }
  }
}
