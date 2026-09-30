import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  registerForm!: FormGroup;
  errorMessage: string = '';

  constructor(private authService: AuthService, private fb: FormBuilder, private router: Router) { }

  ngOnInit() {
    this.registerForm = this.fb.group({
      email: ['', Validators.required ],
      password: [ '', Validators.required ],
      password2: [ '', Validators.required ]

    })
    
  }

  
  onSubmit() {

    const email = this.registerForm.get('email')?.value;
    const password = this.registerForm.get('password')?.value;
    
    this.authService.register(email, password).subscribe({
      next: response => {
        this.router.navigate(['/profile']);
      },
      error: (err) => {
        if (err.code == 'auth/email-already-in-use') {
          this.errorMessage = 'The provided email already exist';
        }
      }
    })
  }

}
