import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {User} from "../Model/user";
@Injectable({
  providedIn: 'root'
})
export class ChessServiceService {

  constructor(private http:HttpClient) { }
  private apiurl = 'http://localhost:8080';
  getFenByID(id:string){
    return this.http.get<String[]>(this.apiurl + '/puzzle/getFenByPuzzleId/' + id);
  }
  getMovesByID(id:string){
    return this.http.get<String[]>(this.apiurl + '/puzzle/getMovesByPuzzleId/' + id);
  }
  setChessPuzzleData(csvFile: File) {
    const formData: FormData = new FormData();
    formData.append('puzzleFile', csvFile);

    return this.http.post(`${this.apiurl}/puzzle/uploadData`, formData);
  }
  getAllIDs(){
    return this.http.get<String[]>(this.apiurl+ '/puzzle/getAllPuzzleIds');
  }
  getAllThemes(){
    return this.http.get<String[]>(this.apiurl + '/puzzle/getAllThemes');
  }
  increasePassed(username:string, puzzleId:string){
    const requestBody = {username:username, puzzleId:puzzleId}
    return this.http.post(this.apiurl + '/puzzle/increasePassed',requestBody);
  }
  getAnerkennung(username:string){
    return this.http.get<String[]>(this.apiurl + '/puzzle/getAnerkennung/' + username)  }
  }
