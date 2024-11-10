import { Component } from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {UserServiceService} from "../../Service/user-service.service";
import {HttpClient} from "@angular/common/http";
import {User} from "../../Model/user";



//import {UserServiceService} from "../../Service/user-service.service";



// export let benutzerEingabe: string = "";

 // export function setBenutzerEingabe(eingabe: string){
 //   benutzerEingabe = eingabe;
 // }


@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent {


  user: User;






  constructor(
    private builder: FormBuilder,
    private router: Router,
    private toastr: ToastrService,
    private service: UserServiceService,
    private http: HttpClient,
  ) {
    this.user = new User()
  }

  authform = this.builder.group({

    inputCode:[
      '',
      [
        Validators.required,
        Validators.minLength(6),
        Validators.maxLength(6),
        Validators.pattern(/^[a-zA-Z0-9]+$/),
      ],
    ],



});



  auth(){
    const loggedUser = localStorage.getItem('currentUser' as string);

    //const inputCode = document.getElementById('inputCode');


    if(this.authform.valid && loggedUser !== null && this.authform.value.inputCode !== null && this.authform.value.inputCode !== undefined){
      localStorage.setItem('inputCode' , this.authform.value.inputCode);
      const inputCode = localStorage.getItem('inputCode');

      this.service.ProceedAuth(loggedUser, this.authform.value.inputCode).subscribe(
        (response) => {
          // Erfolgreiche Antwort
          this.router.navigate(['/lobby']);
        },
        (error) => {
          // Fehlerbehandlung
          this.toastr.error('Two-factor authentication failed. Please double-check your authentication code and try again.');
        }
      );




    }


  }

}
