import {Component, OnDestroy, OnInit} from '@angular/core';
declare var Chessboard :any;
import {Chess} from 'chess.js';
import {GameService} from "../../Service/game.service";
import {Router} from "@angular/router";





@Component({
  selector: 'app-againstbot',
  templateUrl: './againstbot.component.html',
  styleUrls: ['./againstbot.component.css']
})
export class AgainstbotComponent implements OnInit,OnDestroy{
  fen = localStorage.getItem('fen');
  botfen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
  gameName = localStorage.getItem('botgamename')!;
  enemy = localStorage.getItem('enemy')!;
  loggedUser = localStorage.getItem('currentUser')!;
  difficulty = parseInt(localStorage.getItem('diff')!,10);
  botmove:any;
  board: any;
  chess: any;
  refreshInterval:any;
  gameFens: String[] | any;
  key:any;
  timer1:any;
  timer2:any;
  timerzero= false;
  constructor(private gameService: GameService, private router: Router) {
  }



  ngOnInit() {

    const config = {
      draggable: true,
      dropOffBoard: 'snapback',
      position: 'start',
      sparePieces: false,
      onDrop: this.onDrop,
      onDragStart: this.onDragStart,
      onSnapEnd: this.onSnapEnd,
      orientation: 'white'
    }
    this.board = Chessboard('board', config);

    this.board.position(this.botfen);
    this.chess = new Chess();
    this.gameService.initializeBot().subscribe();

    this.refreshInterval = setInterval(()=>{this.timer()},1000);
  console.log(this.gameName)
    this.gameService.openGame(this.gameName).subscribe(data => {
      this.gameFens = data;
      this.fen = this.gameFens[this.gameFens.length - 1];
      this.board.position(this.fen);
      this.chess.load(this.fen);});
  }
  ngOnDestroy() {
    clearInterval(this.refreshInterval);
    this.gameService.deactivateBot().subscribe();
  }
  timer(){

    this.gameService.getTimer(this.gameName).subscribe(data=>{
      if(data[0] === 0 || data[1] === 0){
        this.timerzero = true;
      }
      this.timer1 = this.formatTime(data[0]);
      this.timer2 = this.formatTime(data[1]);
      if(this.timer1 === '0:00'){
        this.surrender();
      } else if (this.timer2 === '0:00'){
        //this.gameService.surrender(this.gameName!,this.enemy!,this.loggedUser!).subscribe()
        this.router.navigate(['/lobby']);
      }
    })
  }
  formatTime(time:number){

    const minutes = Math.floor(time/60);
    const seconds = time%60;
    const formattime = `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
    return formattime;
  }

  onDrop = (source: string, target: string) => {

    try {
      if (source === target || this.timerzero) {
        return 'snapback';
      }
      if(!this.chess.isGameOver()){
        this.chess.move({from: source, to: target,promotion:'q'});
        this.gameService.switchTimer(this.gameName!).subscribe();
        const san = source + target;
        this.fen = this.chess.fen();
        this.updateStatus();
        if(this.chess.isGameOver()){
          this.gameService.addPoints(this.loggedUser).subscribe();
          this.gameService.surrender(this.gameName!,"Bot Yasir", this.loggedUser).subscribe();
          this.router.navigate(['/lobby']);
        }
        this.gameService.sendFenAndSan(this.gameName!, this.fen!, san).subscribe(
          (response) => {
            this.gameService.calculateBestMove(this.gameName!, this.difficulty).subscribe(data=>{
              this.botmove = data;
              const from = this.botmove.charAt(0) + this.botmove.charAt(1);
              const to = this.botmove.charAt(2) + this.botmove.charAt(3);
              this.botmove = from + '-' + to;
              this.board.move(this.botmove);
              this.chess.move({from:from,to:to, promotion:'q'});
              this.fen = this.chess.fen();
              this.updateStatus();
              if(this.chess.isGameOver()){
                this.surrender();
              }
              this.gameService.sendFenAndSan(this.gameName!, this.fen!, from + to).subscribe(
                (response) => {
                  console.log('Erfolg:', response);
                },
                (error) => {
                  console.error('HTTP error:', error);
                }
              );
             this.gameService.switchTimer(this.gameName!).subscribe();
            });
          },
          (error) => {
            // Fehlerbehandlung
            //this.toastr.warning('Es geht nicht!')
            console.error('HTTP error:', error);
          }
        );

        // return 'snapback';
        // this.chess.move({from:source,to:target})
       // console.log(this.chess.ascii());

        return;
      }else{
        return this.key = true;
      }} catch (Exception) {
      return 'snapback';
    }
  }

  onDragStart = (

    source: string,
    piece: string,
    position: any,
    orientation: string
  ): boolean => {
    // Assuming there is a 'game' variable declared somewhere with a 'game_over' and 'turn' method
    if (this.chess.isGameOver() || this.timerzero) return false;

    if (
      (this.chess.turn() === 'w' && piece.search(/^b/) !== -1) ||
      (this.chess.turn() === 'b')
    ) {
      return false;
    }
    return true;
  };
  onSnapEnd = (): void => {

    this.board.position(this.chess.fen());
  };
  updateStatus = (): void => {

    let status = '';

    let moveColor = 'White';
    if (this.chess.turn() === 'b') {
      moveColor = 'Black';
    }

    // checkmate?
    if (this.chess.isCheckmate()) {
      status = `Game over, ${moveColor} is in checkmate.`;
    }

    // draw?
    else if (this.chess.isDraw()) {
      status = 'Game over, drawn position';
    }

    // game still on
    else {
      status = `${moveColor} to move`;

      // check?
      if (this.chess.isCheck()) {
        status += `, ${moveColor} is in check`;
      }
    }

    // Assuming you want to display or use the 'status' variable somewhere
    console.log(status); // or update your UI with the status
  };

  surrender(){
    this.key = true;
    this.gameService.surrender(this.gameName!,this.loggedUser!,"Bot Yasir").subscribe();
    this.gameService.removePoints(this.loggedUser).subscribe();
    setTimeout(()=>{this.router.navigate(['/lobby'])},1000);

  }
  remis(){

    this.gameService.sendDrawRequest(this.gameName!,this.loggedUser,"Bot Yasir").subscribe(
      (response) => {
        this.gameService.acceptDrawRequest(this.gameName!).subscribe(
          (response) => {
            this.router.navigate(['/lobby'])
          },
          (error) => { }
        );
      },
      (error) => { }
    );
  }

}
