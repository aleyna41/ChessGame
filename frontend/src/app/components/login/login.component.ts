import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { HttpClient } from '@angular/common/http';
import { UserServiceService } from 'src/app/Service/user-service.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],

})
export class LoginComponent {

  constructor(
    private builder: FormBuilder,
    private router: Router,
    private toastr: ToastrService,
    private service: UserServiceService,
    private http: HttpClient
  ) {
  }

  loginform = this.builder.group({
    username: this.builder.control('', Validators.required),
    password: this.builder.control('', Validators.required)
  })

  proceedlogin() {
    //username wird nicht überprüft(if bedingung mit error fehlt)
    if (this.loginform.valid) {

          localStorage.setItem('currentUser', this.loginform.value.username as string);
          localStorage.setItem('friendPointer', this.loginform.value.username as string);
          this.service.ProceedLogin(this.loginform.value).subscribe(
            (response) => {
              // Erfolgreiche Antwort
              console.log('Erfolg:', response);
              this.router.navigate(['/auth']);
            },
            (error) => {
              // Fehlerbehandlung
              this.toastr.error('Login failed. Please check your information and try again.')
              console.error('HTTP error:', error);
            }
          );
        } else {
          this.toastr.error('Please fill in all the required information.');
        }
      }
}
