import {Component, OnInit} from '@angular/core';
import {GameService} from "../../Service/game.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-streamlist',
  templateUrl: './streamlist.component.html',
  styleUrls: ['./streamlist.component.css']
})
export class StreamlistComponent implements OnInit{
  games: any;


constructor(private gameService:GameService, private router:Router) {
}
ngOnInit() {
  this.gameService.getStreams().subscribe(data =>{this.games = data});
}

  goToGameStream(i: number) {
    const selectedGame = this.games[i];
    this.router.navigate([`/livestream/${selectedGame}`]);
  }
}
