import { Component } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth';
import { type RegisterRequest } from '../models/auth';

@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  userEmail: string = '';
  userPassword: string = '';
  confirmPassword: string = '';
  backendErrorMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}

  passwordsMatch(): boolean {
    if (!this.userPassword || !this.confirmPassword) {
      return true;
    }
    return this.userPassword === this.confirmPassword;
  }

  onRegisterClick(form: NgForm) {
    if (form.valid && this.passwordsMatch()) {
      const userData: RegisterRequest = {
        email: this.userEmail,
        password: this.userPassword,
      };
      this.authService.register(userData).subscribe({
        next: (response) => {
          this.authService.saveToken(response.token);
          this.backendErrorMessage = '';

          this.router.navigate(['/map']);
        },
        error: (error) => {
          this.backendErrorMessage = error.message;
        }
      });
    }
  }
}
