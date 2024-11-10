import {GameService} from "../../Service/game.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Chess} from "chess.js";
import { Component, OnInit } from '@angular/core';
declare var Chessboard :any;

@Component({
  selector: 'app-repeat-pgn-game',
  templateUrl: './repeat-pgn-game.component.html',
  styleUrls: ['./repeat-pgn-game.component.css']
})
export class RepeatPgnGameComponent implements OnInit{
  white: string = 'White';
  black: string = 'Black';
  result: string = '';
  name: string|null = '';

  moveIndex: number = -1;
  san: string[] = [];
  board:any;
  chess:any;
  isFlipped: boolean = false;

  constructor(private gameService:GameService,
              private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.name = localStorage.getItem('name');
    const storedSans = localStorage.getItem('pgnSans');
    if (storedSans) {
      try {
        const parsedPgnSans = JSON.parse(storedSans);
        if (Array.isArray(parsedPgnSans) && parsedPgnSans.every((item) => typeof item === 'string')) {
          this.san = parsedPgnSans as string[];
        }
        if (this.san.length > 0) {
          console.log("if")
          this.result = this.san[this.san.length - 1];
        }
      } catch (error) {
        console.error('Fehler bei der Konvertierung:', error);
      }
    }
    const config = {
      draggable: false,
      position: 'start',
      sparePieces: false,
      orientation:'white'
    }
    this.board = Chessboard('board', config);
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
}
