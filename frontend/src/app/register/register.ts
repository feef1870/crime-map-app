import { Component } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { RouterLink } from '@angular/router';

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

  passwordsMatch(): boolean {
    if (!this.userPassword || !this.confirmPassword) {
      return true;
    }
    return this.userPassword === this.confirmPassword;
  }

  onRegisterClick(form: NgForm) {
    if (form.valid && this.passwordsMatch()) {
      console.log(this.passwordsMatch());
    }
  }
}
