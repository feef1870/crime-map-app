import { Component } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth';
import { LoginRequest } from '../models/auth';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  userEmail: string = '';
  userPassword: string = '';
  backendErrorMessage: string = '';

  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}

  onLoginClick(form: NgForm) {
    if (form.valid) {
      const credentials: LoginRequest = {
        email: this.userEmail,
        password: this.userPassword,
      };

      this.authService.login(credentials).subscribe({
        next: (response) => {
          console.log(response);
          this.authService.saveToken(response.token);
          this.backendErrorMessage = '';
        },

        error: (err) => {
          this.backendErrorMessage = err.message;
        },
      });
    }
  }
}
