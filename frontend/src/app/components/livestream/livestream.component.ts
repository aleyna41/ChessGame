import {Component, OnDestroy, OnInit} from '@angular/core';
import {GameService} from "../../Service/game.service";
import {ActivatedRoute, Router} from '@angular/router';
declare var Chessboard :any;
@Component({
  selector: 'app-livestream',
  templateUrl: './livestream.component.html',
  styleUrls: ['./livestream.component.css']
})
export class LivestreamComponent implements OnInit,OnDestroy{
  board:any
  refreshInterval:any
  gameFens: any
  fen: any
  gameName:any
  players:any
  whiteplayer:any
  blackplayer:any
  timers:any
  whitetimer:any
  blacktimer:any
  isFlipped = false;
  gameId = 0
  constructor(private gameService:GameService,private route: ActivatedRoute, private router: Router) { }
  ngOnInit(){
    this.route.paramMap.subscribe(params => {
      this.gameName = params.get('gameId');});
    const config = {
      draggable: false,
      position: 'start',
      sparePieces: false,
      orientation:'white'
    }
    this.gameService.getPlayersByGamename(this.gameName).subscribe(data =>{
      this.players = data;
      this.whiteplayer = this.players[0];
      this.blackplayer = this.players[1];
    });
    this.board = Chessboard('board', config);
    this.refreshInterval = setInterval(()=>{
      this.refreshBoard();
    },100);
    this.gameService.getGameId(this.gameName).subscribe(gameId => {
      this.gameId = gameId;
    });
  }
  refreshBoard(){
    this.gameService.openGame(this.gameName).subscribe(data =>{this.gameFens = data;
      this.fen = this.gameFens[this.gameFens.length -1];
      this.board.position(this.fen);
    });
    this.gameService.getResult(this.gameName).subscribe(data=>{
      if(data === '1-0' || data === '0-1'){
        this.router.navigate(['/game-result/' + this.gameId])
      }else if(data === '1/2-1/2'){
        this.router.navigate(['/lobby'])
      }
    })
    this.timer();
  }
  timer(){

    this.gameService.getTimer(this.gameName).subscribe(data=> {
      this.timers = data
        this.whitetimer = this.formatTime(this.timers[0])
      console.log(this.whitetimer)
        this.blacktimer = this.formatTime(this.timers[1])
    });
  }
  formatTime(time:number){
    const minutes = Math.floor(time/60)
    const seconds = time%60
    const formattime = `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`
    return formattime
  }
  flipBoard(){
    this.board.flip()
    this.isFlipped = !this.isFlipped
  }
  ngOnDestroy() {
    clearInterval(this.refreshInterval);
  }
}
