import { Component, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { HeaderComponent } from "./components/header/header.component";
import { filter } from 'rxjs';
import { AuthService } from './auth/auth.service';
import { FooterComponent } from './components/footer/footer.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, HeaderComponent, FooterComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  constructor() {
    this.authService.isLoggedIn$
      .pipe(filter(loggedIn => loggedIn === false))
      .subscribe(() => {
        const url = this.router.url;

        // if user is on protected pages, kick to login
        if (url.startsWith('/invoices') || url.startsWith('/profile')) {
          this.router.navigate(['/login']);
        }
      });
  }
}
