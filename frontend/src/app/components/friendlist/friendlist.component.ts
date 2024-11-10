import {Component, OnDestroy, OnInit} from '@angular/core';
import { User } from 'src/app/Model/user';
import {UserServiceService} from "../../Service/user-service.service";
import {MatRadioChange} from "@angular/material/radio";
import {FormBuilder, Validators} from "@angular/forms";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";
import {FriendServiceService} from "../../Service/friend-service.service";

@Component({
  selector: 'app-friendlist',
  templateUrl: './friendlist.component.html',
  styleUrls: ['./friendlist.component.css'],
})

//HALLOOOOOOO

export class FriendlistComponent implements OnInit,OnDestroy{
  loggedUser =localStorage.getItem('currentUser' as string);
  friendPointer = localStorage.getItem('friendPointer');
  isPrivate:any = false;
  friends: String[] | any;
  friendrequests: String[] | any;
  refreshInterval: any;
  test:string[] = ['lobby', 'test2'];
    constructor(
      private userService: UserServiceService,
      private friendService: FriendServiceService,
      private builder: FormBuilder,
      private toastr: ToastrService,
      private router: Router
    )
    { }

  addFriendForm =this.builder.group({
    friendInput: this.builder.control(
    '',
    Validators.compose([Validators.required, Validators.minLength(5)])
  )
  });
  ngOnInit() {
    this.refreshTables();
    this.userService.isPrivate(this.loggedUser!).subscribe(data =>{this.isPrivate = data;})
    this.refreshInterval = setInterval(()=>{
      this.refreshTables();
    }, 5000);
  }
  refreshTables(){
    if(this.loggedUser === null){
      return;
    }
  this.friendService.GetFriends(this.loggedUser).subscribe(data =>{this.friends=data});
   this.friendService.GetFriendRequests(this.loggedUser).subscribe(data =>{this.friendrequests=data});
  }
  ngOnDestroy() {
      clearInterval(this.refreshInterval);
  }

  sendFriendRequest(){
    if(this.loggedUser === this.addFriendForm.value.friendInput){
      this.toastr.warning('Nicht selber adden!')
      return;
    }
      if(this.addFriendForm.valid&&this.loggedUser !== null && this.addFriendForm.value.friendInput !== null && this.addFriendForm.value.friendInput!== undefined){
      this.friendService.sendFriendRequest(this.loggedUser, this.addFriendForm.value.friendInput).subscribe(
        (response) => {
          // Erfolgreiche Antwort
          this.toastr.success('Yippie');
          console.log('Erfolg:', response);
        },
        (error) => {
          // Fehlerbehandlung
          this.toastr.warning('Es geht nicht!')
          console.error('HTTP error:', error);
        }
      );
    }
    else{
      this.toastr.warning('Username wrong');
    }
  }
  deleteFriend(index:number){

    const deletedFriend = this.friends[index];
    console.log(deletedFriend);
    this.friendService.deleteFriend(this.loggedUser, deletedFriend).subscribe(
      (response) => {
        // Erfolgreiche Antwort
      },
      (error) => {
        // Fehlerbehandlung
      }
    );
    this.friendService.deleteFriend(deletedFriend,this.loggedUser!).subscribe(
      (response) => {
        // Erfolgreiche Antwort
      },
      (error) => {
        // Fehlerbehandlung
      }
    );
  }
  acceptFriendRequest(index:number){

  const addedFriend =this.friendrequests[index];
    if(this.loggedUser=== null){
      return;
    }
    this.friendService.acceptFriendRequest(addedFriend,this.loggedUser).subscribe(
      (response) => {
        // Erfolgreiche Antwort
        this.toastr.success('Yippie');
        console.log('Erfolg:', response);
      },
      (error) => {
        // Fehlerbehandlung
        this.toastr.warning('Es geht nicht!')
        console.error('HTTP error:', error);
      }
    );
  }
  denyFriendRequest(index:number){

    const  deniedFriend = this.friendrequests[index];
    if(this.loggedUser=== null){
      return;
    }
    this.friendService.denyFriendRequest(deniedFriend, this.loggedUser).subscribe(
      (response) => {
        // Erfolgreiche Antwort
        this.toastr.success('Yippie');
        console.log('Erfolg:', response);
      },
      (error) => {
        // Fehlerbehandlung
        this.toastr.warning('Es geht nicht!')
        console.error('HTTP error:', error);
      }
    );
  }
  onRadioChange(event: MatRadioChange){

    if
    ( event.value== 'private') {
      this.isPrivate = true;
    } else {
      this.isPrivate = false;
    }
    this.userService.updatePrivacySettings(this.loggedUser, this.isPrivate).subscribe(
      (response) => {
        // Erfolgreiche Antwort
        this.toastr.success('Yippie');
        console.log('Erfolg:', response);
      },
      (error) => {
        // Fehlerbehandlung
        this.toastr.warning('Es geht nicht!')
        console.error('HTTP error:', error);
      }
    );
  }
  gotoFriend(index:number){
     const friendProfile = this.friends[index];
   localStorage.setItem("friendPointer", friendProfile);
    this.router.navigate([`/profile/${friendProfile}`]);
  }
}


