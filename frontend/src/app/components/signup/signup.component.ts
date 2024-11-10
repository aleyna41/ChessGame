import { Component } from '@angular/core';
import {User} from "../../Model/user";
import {UserServiceService} from "../../Service/user-service.service";
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { HttpClient } from '@angular/common/http';



function displayImage() {
  var fileInput = document.getElementById('image') as HTMLInputElement;

    if (fileInput.files && fileInput.files[0]) {
      var reader = new FileReader();

      reader.readAsDataURL(fileInput.files[0]);
    }

  }
@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {

  constructor(
    private builder: FormBuilder,
    private router: Router,
    private toastr: ToastrService,
    private service: UserServiceService,
    private http: HttpClient
  ) {
  }

  registerform = this.builder.group({
    username: this.builder.control(
      '',
      Validators.compose([Validators.required, Validators.minLength(5)])
    ),
    firstname: this.builder.control(
      '', Validators.compose([Validators.required, Validators.pattern('[a-zA-Z ]*'),
      ])
    ),
    lastname: this.builder.control(
      '', Validators.compose([Validators.required, Validators.pattern('[a-zA-Z ]*'),
      ])
    ),
    birthday: this.builder.control(
      '',Validators.compose([Validators.required, Validators.pattern('^\\d{4}[\\-\\-](0?[1-9]|1[012])[\\-\\-](0?[1-9]|[12][0-9]|3[01])$'),
    ])
    ),
    password: this.builder.control(
      '',
      Validators.compose([
        Validators.required,
        Validators.minLength(5)
      ])
    ),
    email: this.builder.control(
      '',
      Validators.compose([Validators.required, Validators.email])
    ),

  });

  proceedregister() {
    if (this.registerform.valid) {
      let profilePic = document.getElementById("image");
      //alert(this.registerform.value.username + " " + this.registerform.value.firstname + " " +this.registerform.value.lastname + " " +this.registerform.value.email +" " + this.registerform.value.password + " " +this.registerform.value.birthday);
      this.service.ProceedRegister(this.registerform.value).subscribe(
        (response) => {
          // Erfolgreiche Antwort
          this.toastr.success('Sign up successful! Welcome to SchachEP.');
          this.router.navigate(['login']);
        },
        (error) => {
          // Fehlerbehandlung
          this.toastr.error('Sign-up failed. Please check your information and try again.');
        }
      );

    } else {
      this.toastr.error('Please fill in all the required information.');
    }
  }
}
