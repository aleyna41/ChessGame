import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {LeaderboardServiceService} from "../../Service/leaderboard-service.service";
import {UserServiceService} from "../../Service/user-service.service";

@Component({
  selector: 'app-leaderboard',
  templateUrl: './leaderboard.component.html',
  styleUrls: ['./leaderboard.component.css']
})
export class LeaderboardComponent implements OnInit{
  players: any; // MUSS WAHRSCHEINLICH GEÄNDERT WERDEN?
  testplayers: string[] = ['WangoNummerEins', 'WangoNummerZwei'];
  friendPointer = localStorage.getItem('friendPointer');
constructor(
  private router: Router,
  private userService: UserServiceService,
) {}
ngOnInit(){
this.userService.getLeaderboard().subscribe(data =>{this.players = data});
}
gotoPlayer(index:number){
  const playerProfile = this.players[index].username;
  localStorage.setItem("friendPointer", playerProfile);
  this.friendPointer =  localStorage.getItem('friendPointer');
  this.router.navigate([`/profile/${playerProfile}`]);
}
}

