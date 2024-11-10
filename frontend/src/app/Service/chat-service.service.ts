import { Injectable } from '@angular/core';
import {filter, map, Observable, share, Subject} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {ChatInfo} from "../Model/chatInfo";
import {Message} from "../Model/message";
import {User} from "../Model/user";

@Injectable({
  providedIn: 'root'
})
export class chatService {

  constructor(private http: HttpClient) {}

  private apiurl = 'http://localhost:8080';

  createPrivateChat(creatorName: string, friendName: string): Observable<ChatInfo>{
    const requestBody = {
      creatorName: creatorName,
      friendName: friendName
    };
    alert(creatorName + " bliblablub " + friendName);
    return this.http.post<ChatInfo>(this.apiurl + "/privateChat", requestBody)
  }

  sendPrivateChatMessage(message: Message) {
    return this.http.post<ChatInfo>(this.apiurl + '/sendMessage', message);
  }
  sendGroupChatMessage(message: Message) {
    return this.http.post<ChatInfo>(this.apiurl + '/sendGroupMessage', message);
  }

  getChatHistory(loggedUser: string, friendName: string, chatType: string): Observable<Message[]> {
    const url = `${this.apiurl}/getChatHistory/${loggedUser}/${friendName}?chatType=${chatType}`;
      return this.http.get<Message[]>(url);
  }

  GetPrivateChats(loggedUser: string): Observable<ChatInfo[]> {
    return this.http.get<ChatInfo[]>(this.apiurl + `/getPrivateChats/` + loggedUser);
  }

  GetGroupChats(loggedUser: string): Observable<ChatInfo[]> {
    return this.http.get<ChatInfo[]>(this.apiurl + `/getGroupChats/` + loggedUser);
  }

  createGroupChat(groupName: string, loggedUser: string, selectedFriends: String[]): Observable<ChatInfo> {
    const requestBody = {
      groupName: groupName,
      creator: loggedUser,
      members: selectedFriends
      };
    return this.http.post<ChatInfo>(this.apiurl + '/groupChat', requestBody);
    }

  deleteChat(loggedUser: string, friendName: string, chatType: string) {
    const requestBody = {loggedUser: loggedUser, friendName: friendName, chatType: chatType};
    return this.http.post(this.apiurl + '/deleteChat', requestBody);
  }

  getChessClubs(){
    return this.http.get<String[]>(this.apiurl + `/getChessClubNames`);
  }
  getMyChessClubs(username:string){
    return this.http.get<String[]>(this.apiurl + '/getMyChessClubNames/' + username)
  }

  joinChessClub(loggedUser: string, clubName: String){
    const requestBody = {member: loggedUser, clubName: clubName};
    return this.http.post(this.apiurl + '/joinChessClub', requestBody);
  }

  createChessClub(loggedUser: string, clubName: string){
    const requestBody = {member: loggedUser, clubName: clubName};
    return this.http.post(this.apiurl + '/createChessClub', requestBody);
  }

  sendClubMessage(message: Message) {
      return this.http.post<ChatInfo>(this.apiurl + '/sendClubMessage', message);
  }

  markMessagesAsSeen(loggedUser: String, message: Message) {
    const url = `${this.apiurl}/seen/${loggedUser}`;
    return this.http.post(url, message);
  }

  deleteMessage(origMsg: Message) {
    return this.http.post(this.apiurl + '/deleteMessage',origMsg);
  }

  editMessage(editingMessage: String, msgId: number) {
    alert(msgId);
    return this.http.post(this.apiurl + `/editMessage/${editingMessage}/${msgId}`, null)
  }
}
