import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {User} from "../Model/user";

@Injectable({
  providedIn: 'root'
})
export class FriendServiceService {

  constructor(private http: HttpClient) { }

  private apiurl = 'http://localhost:8080';

  GetFriends(loggedUser: String) {
    return this.http.get<String[]>(this.apiurl + `/getFriends/` + loggedUser );
  }

  GetFriendRequests(loggedUser: String) {
    return this.http.get<String[]>(this.apiurl + '/getPendingFriendRequests/' + loggedUser)
  }

  sendFriendRequest(senderName: String, receiverName: String) {
    const requestPayload =
      {senderName: senderName,
        receiverName: receiverName
      };
    return this.http.post(this.apiurl + '/sendFriendRequest',requestPayload);
  }

  deleteFriend(loggedUser: any, deletedFriend: String) {
    return this.http.get(this.apiurl + `/removeFriend/${deletedFriend}/${loggedUser}`);
  }

  acceptFriendRequest(acceptedFriend: String,loggedUser: String) {
    const requestBody =
      {senderName: acceptedFriend,
        receiverName: loggedUser
      }
    return this.http.post(this.apiurl + '/acceptFriendRequest' ,requestBody);
  }

  denyFriendRequest(deniedFriend: String,loggedUser: String) {
    const requestBody =
      {senderName: deniedFriend,
        receiverName: loggedUser
      }
    return this.http.post(this.apiurl + '/rejectFriendRequest', requestBody);
  }
}
