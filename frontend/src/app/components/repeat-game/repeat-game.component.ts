import { Component, OnInit } from '@angular/core';
declare var Chessboard :any;
import {Chess} from 'chess.js';
import {GameService} from "../../Service/game.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-repeat-game',
  templateUrl: './repeat-game.component.html',
  styleUrls: ['./repeat-game.component.css']
})
export class RepeatGameComponent {
  friendPointer: string = '';
  enemy: string = '';
  gameId: number = 0;
  white: string = '';
  black: string = '';
  result: string|null = '';

  moveIndex: number = 0;
  san: String[] = [];
  board:any;
  chess:any;
  isFlipped: boolean = false;

  constructor(private gameService:GameService,
              private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.friendPointer = this.route.snapshot.paramMap.get('profileUser')!;
    this.enemy = this.route.snapshot.paramMap.get('enemy')!;
    this.gameId = +this.route.snapshot.paramMap.get('gameId')!;
    this.result = localStorage.getItem('result');

    const config = {
      draggable: false,
      position: 'start',
      sparePieces: false,
      orientation:'white'
    }
    this.board = Chessboard('board', config);

    this.gameService.getGameSans(this.gameId).subscribe(data => {
      this.san = data.map(move => move.replace(/'/g, ''));
      this.moveIndex = -1;
      this.updateBoardPosition();
    });

    this.gameService.getWhite(this.gameId).subscribe(data => {
      this.white = data;
      this.getBlack();
    });

  }

  goForwards() {
    if (this.moveIndex < this.san.length - 1) {
      this.moveIndex++;
      this.updateBoardPosition();
    }
  }

  goBackwards() {
    if (this.moveIndex > -1) {
      this.moveIndex--;
      this.updateBoardPosition();
    }
  }

  private updateBoardPosition() {
    this.chess = new Chess();
    for (let i = 0; i <= this.moveIndex; i++) {
      this.chess.move(this.san[i]);
    }
    this.board.position(this.chess.fen());
  }

  flipBoard() {
    this.board.flip();
    this.isFlipped = !this.isFlipped;
  }

  goBack(){
    const savedPath = localStorage.getItem('path');
    if (savedPath) {
      this.router.navigate(([savedPath]));
    }
  }

  getBlack(){
    if(this.white === this.enemy){
      this.black = this.friendPointer;
    } else this.black = this.enemy;
  }

}
