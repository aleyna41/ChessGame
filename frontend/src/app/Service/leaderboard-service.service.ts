import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
@Injectable({
  providedIn: 'root'
})
export class LeaderboardServiceService {

  constructor(private http: HttpClient) { }

  private apiurl = 'http://localhost:8080'

  getSortedPlayers(){
    return this.http.get(this.apiurl + '/sorted');
  }
}
