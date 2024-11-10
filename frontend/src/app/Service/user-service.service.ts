import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../Model/user";
import {gamehistory} from "../Model/gamehistory";

@Injectable({
  providedIn: 'root'
})
export class UserServiceService {


  constructor(private http: HttpClient) { }

  private apiurl = 'http://localhost:8080';

  GetAll() {
    return this.http.get(this.apiurl);
  }

  GetByCode(username: string) {
    return this.http.get<User>(`${this.apiurl}/profile/`+ username);
  }
  GetUserByName(username:string){
    return this.http.get<User>(this.apiurl+ '/getUserByName/' + username)
  }
  ProceedRegister(inputdata: any) {

    return this.http.post(this.apiurl + '/registerUser', inputdata);

  }
  ProceedLogin(loginForm:any){
    return this.http.post(this.apiurl + "/login", loginForm);
  }

  ProceedAuth(username: string, inputcode: string){
      const requestBody = {
        username: username,
        inputCode: inputcode
      };
      return this.http.post(this.apiurl + "/2FA", requestBody);
    }

  Updateuser(code: any, inputdata: any) {
    return this.http.put(this.apiurl + '/' + code, inputdata);
  }

  updatePrivacySettings(loggedUser: any, isPrivate: boolean) {
    const requestBody = { username: loggedUser, isPrivate: isPrivate}
    return this.http.post(this.apiurl + '/updatePrivacy', requestBody);
  }

  acceptGameRequest(gameName: string) {
    return this.http.post(this.apiurl + '/game/acceptGameRequest', gameName);
  }

  denyGameRequest(gameName: string) {
    return this.http.post(this.apiurl + '/game/rejectGameRequest', gameName);
  }

  isPrivate(username:any){
    return this.http.get(this.apiurl + `/getIsPrivate/${username}`);
  }

  setProfilePicture(username: string, profilePicture: File) {
    const formData: FormData = new FormData();
    formData.append('username', username);
    formData.append('profileImage', profilePicture, profilePicture.name);
    return this.http.post(`${this.apiurl}/setProfilePicture`, formData);
  }

  getProfilePicture(username: string): Observable<string> {
    return this.http.get<string>(`${this.apiurl}/getProfilePicture/${username}`);
  }

  getLeaderboard(){
    return this.http.get(this.apiurl + '/getLeaderboard');
  }
  getGameInvites(loggedUser: String){
    return this.http.get<String[]>(this.apiurl + '/game/getPendingGameRequests/' + loggedUser);
  }
  startGameRequest(loggedUser: String, enemy: String,gamename: String, time: number ){
    const requestBody = {
      gameName: gamename,
      player1: loggedUser,
      player2: enemy,
      time: time,
    }
    return this.http.post(this.apiurl + '/game/sendGameRequest', requestBody);

  }
  createBotGame(gameName:string, loggedUser:string){
    const requestBody = {
      gameName: gameName,
      player1: loggedUser,
      player2: 'Bot Yasir',
      time: 15,
    }

    return this.http.post(this.apiurl + '/createBotGame', requestBody)
  }

  getGameHistory(loggedUser: String){
    return this.http.get<gamehistory[]>(this.apiurl + '/getGameHistory/' + loggedUser);
  }

  logoutUser(loggedUser: String) {
    return this.http.post(this.apiurl + "/logout", loggedUser);
  }
}
