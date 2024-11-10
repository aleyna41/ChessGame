import {Component, OnDestroy, OnInit} from '@angular/core';
declare var Chessboard :any;
import {Chess} from 'chess.js';
import {GameService} from "../../Service/game.service";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";





@Component({
  selector: 'app-gameboard',
  templateUrl: './gameboard.component.html',
  styleUrls: ['./gameboard.component.css']
})
export class GameboardComponent implements OnInit,OnDestroy {
  fen = localStorage.getItem('fen');
  gameName = localStorage.getItem('gameName');
  enemy = localStorage.getItem('enemy');
  player = localStorage.getItem('player');
  player1 = this.player;
  player2 = this.enemy;
  loggedUser = localStorage.getItem('currentUser')!;
  board: any;
  chess: any;
  gameFens: String[] | any;
  timers:any;
  timer1:any;
  timer2:any;
  result:any;
  key:any;
  from = 'e4';
  to= 'e4';
  timerzero= false;
  isDrawRequested:any
  bestmove:any
  gamedey = 0

  constructor(private gameService: GameService, private router: Router, private toastr: ToastrService,) {
  }

  moves: any;
  refreshInterval: any;


  ngOnInit() {

    const draw = document.getElementById('draw');
    draw!.style.display = 'none'
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
    console.log(this.board)
    if (this.loggedUser !== this.player) {
      this.board.flip();
      const temp = this.player2;
      this.player2 = this.player1;
      this.player1 = temp;
    }

    this.board.position(this.fen);
    this.chess = new Chess();
    this.chess.load(this.fen);

    this.timer1 = ' '
    this.refreshInterval = setInterval(() => {
      this.refreshBoard();
    }, 1000);


    this.gameService.getGameId(this.gameName!).subscribe(gameId => {
      this.gamedey = gameId;
    });

    this.gameService.initializeBot().subscribe();

    // Wichtige funktionen
    /*
      var fen = board2.fen;       // bekommt den fen vom Board
      board2.position(einFen);    // setzt die Pieces auf einen Fen
      board2.destroy();           // entfernt das Board
      board2.flip();              // flipt die Orientation des Boards (schwarz unten)
      board2.move();              // macht einen move (unklar was in die Klammer muss)
      board2.orientation();       // stellt die Orientierung ein (ich glaube mit 'white' oder 'black'
      board2.resize();            // keine ahnung
      board2.start();             // Orientierung mit 'white'/'black' möglich aber ich glaube man kann noch mehr einstellen
      board2.clear();             // cleart die Pieces vom Board*/
  }

  ngOnDestroy() {
    clearInterval(this.refreshInterval);
    this.gameService.deactivateBot().subscribe()
  }

  refreshBoard() {
    this.gameService.getResult(this.gameName!).subscribe(data =>{
      this.result = data
      if(this.result === '1-0'||this.result==='0-1'){
        this.router.navigate(['/game-result/' + this.gamedey])
      } else if(this.result === '1/2-1/2'){
        this.router.navigate(['/lobby'])
      }
    })
    this.gameService.openGame(this.gameName!).subscribe(data => {
      this.gameFens = data;
      this.fen = this.gameFens[this.gameFens.length - 1];
      this.board.position(this.fen);
      this.chess.load(this.fen);
      this.timer();
    });

    this.gameService.isDrawRequested(this.gameName!).subscribe(data=>{
      if(data !== true){
        const draw = document.getElementById('draw');
        draw!.style.display = 'none'
        localStorage.setItem('drawSent', '')

      }
      else if(data === true && localStorage.getItem('drawSent') !== 'true'){
        const draw = document.getElementById('draw');
        draw!.style.display = ''

      }

    })
  }

  onDrop = (source: string, target: string) => {
    try {
      if (source === target || this.timerzero) {
        return 'snapback';
      }
      if(!this.chess.isGameOver()){
      this.chess.move({from: source, to: target, promotion:'q'})
      const san = source + target;
      this.fen = this.chess.fen();
      this.gameService.sendFenAndSan(this.gameName!, this.fen!, san).subscribe(
        (response) => {
          // Erfolgreiche Antwort
          //this.toastr.success('Yippie');
          console.log('Erfolg:', response);
        },
        (error) => {
          // Fehlerbehandlung
          //this.toastr.warning('Es geht nicht!')
          console.error('HTTP error:', error);
        }
      );
      this.gameService.switchTimer(this.gameName!).subscribe();

      // return 'snapback';
      console.log(this.chess.ascii());
        let squareElement1 = document.querySelector('.square-' + this.from)!as HTMLElement;
        let squareElement2 = document.querySelector('.square-' + this.to)!as HTMLElement;
        if (squareElement1) {
          squareElement1.style.backgroundColor = '';
          squareElement2.style.backgroundColor = '';
        }
        this.updateStatus();
      if(this.chess.isGameOver()){
        this.naturalend()
      }
      return;
    }else{
        return;
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
      (this.chess.turn() === 'b' && piece.search(/^w/) !== -1)
    ) {
      return false;
    } else if (this.loggedUser !== this.player && this.chess.turn() === 'w') {
      return false;
    } else if (this.loggedUser === this.player && this.chess.turn() === 'b') {
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
  startLivestream(){
    this.gameService.startLivestream(this.gameName!).subscribe(
      (response) => {
        // Erfolgreiche Antwort
        this.toastr.success('Livestream wurde gestartet!');
        console.log('Erfolg:', response);
      },
      (error) => {
        // Fehlerbehandlung
        this.toastr.warning('Livestream konnte nicht gestartet werden!')
        console.error('HTTP error:', error);
      }
    );
  }

  timer(){

    this.gameService.getTimer(this.gameName!).subscribe(data=> {
      this.timers = data
      if(this.timers[0] === 0 || this.timers[1] === 0){
        this.timerzero = true;
      }
      if (this.loggedUser === this.player) {
        this.timer1 = this.formatTime(this.timers[0])
        this.timer2 = this.formatTime(this.timers[1])
      } else{
        this.timer2 = this.formatTime(this.timers[0])
        this.timer1 = this.formatTime(this.timers[1])
      }
      if(this.timer1 === '0:00'){
        this.surrender()
      } else if (this.timer2 === '0:00'){
        //this.gameService.surrender(this.gameName!,this.enemy!,this.loggedUser!).subscribe()
        this.router.navigate(['/game-result/' + this.gamedey])
      }
    });
  }
  formatTime(time:number){
    const minutes = Math.floor(time/60)
    const seconds = time%60
    const formattime = `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`
    return formattime
  }
  sendDrawRequest(){
    this.gameService.sendDrawRequest(this.gameName!,this.loggedUser!,this.enemy!).subscribe()
    localStorage.setItem('drawSent', 'true')
  }
  acceptDrawRequest(){
    this.gameService.acceptDrawRequest(this.gameName!).subscribe()
    this.gameService.stopTimer(this.gameName!).subscribe()
    this.router.navigate(['/lobby'])
  }
  denyDrawRequest(){
    this.gameService.denyDrawRequest(this.gameName!).subscribe(
      (response) => {
        // Erfolgreiche Antwort
        console.log('Erfolg:', response);
      },
      (error) => {
        // Fehlerbehandlung
        console.error('HTTP error:', error);
      }

    );
  }
  surrender(){
    this.gameService.surrender(this.gameName!,this.loggedUser!,this.enemy!).subscribe(
      (response) => {
        this.router.navigate(['/game-result/' + this.gamedey])
        this.gameService.addPoints(this.player2!).subscribe()
        this.gameService.removePoints(this.player1!).subscribe()
      },
      (error) => {})
  }

  assist(){
    if (this.loggedUser !== this.player && this.chess.turn() === 'w') {
      this.toastr.warning('It\'s white\'s turn');

    } else if (this.loggedUser === this.player && this.chess.turn() === 'b') {
      this.toastr.warning('It\'s black\'s turn');

    }else{
      this.gameService.calculateBestMoveForAssistant(this.gameName!).subscribe(data =>{
        this.bestmove = data
        console.log(this.bestmove)
          this.from = this.bestmove.charAt(0) + this.bestmove.charAt(1)
          this.to = this.bestmove.charAt(2) + this.bestmove.charAt(3)
          this.bestmove = this.bestmove.charAt(0) + this.bestmove.charAt(1) + '-' + this.bestmove.charAt(2) + this.bestmove.charAt(3)
        const squareId = 'e5';
        let squareElement1 = document.querySelector('.square-' + this.from)!as HTMLElement;
        let squareElement2 = document.querySelector('.square-' + this.to)!as HTMLElement;
        if (squareElement1) {
          squareElement1.style.backgroundColor = '#c41e3a';
          squareElement2.style.backgroundColor = '#8eacf3';
        } else {

          console.error(this.board)
          console.error('Square not found:', squareId);
        }
          this.gameService.payWithPoint(this.loggedUser).subscribe()
      })
    }




  }

  naturalend() {
    if (this.chess.turn === 'w') {
      this.gameService.surrender(this.gameName!,this.loggedUser!,this.enemy!).subscribe()
      this.gameService.addPoints(this.enemy!).subscribe();
      this.gameService.removePoints(this.player!).subscribe();
    } else {
      this.gameService.surrender(this.gameName!,this.player2!,this.enemy!).subscribe()
      this.gameService.addPoints(this.player1!).subscribe();
      this.gameService.removePoints(this.player2!).subscribe();
    }
    this.router.navigate(['/game-result/' + this.gamedey])
  }



}
