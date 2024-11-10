import { Component } from '@angular/core';
import { UserServiceService } from 'src/app/Service/user-service.service';
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontend';

  constructor(
    private router: Router,
    private toastr: ToastrService,
    private service: UserServiceService,
    private http: HttpClient
  ) {
  }

  logoutUser() {
    const loggedUser = localStorage.getItem('currentUser' as string);

    if(loggedUser)
    this.service.logoutUser(loggedUser).subscribe(
      (response) => {
        // Erfolgreiche Antwort
        this.toastr.success('Erfolg: '+ response);
        this.router.navigate(['/homepage']);
      },
      (error) => {
        // Fehlerbehandlung
        this.toastr.warning('Etwas ist schief gelaufen');
        this.router.navigate(['/homepage']);
      }
    );
  }
}
