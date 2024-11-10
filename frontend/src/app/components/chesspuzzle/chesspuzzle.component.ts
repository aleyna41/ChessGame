import { Component, OnInit } from '@angular/core';
declare var Chessboard: any;
import { ChessServiceService } from "../../Service/chess-service.service";
import { Chess } from "chess.js";
import { waitForAsync } from "@angular/core/testing";
import {User} from "../../Model/user";
import {UserServiceService} from "../../Service/user-service.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-chesspuzzle',
  templateUrl: './chesspuzzle.component.html',
  styleUrls: ['./chesspuzzle.component.css']
})
export class ChesspuzzleComponent implements OnInit {

  board: any;
  puzzle: any;
  fens: any;
  fen: any;
  moves:any; //['c2-c4','e7-e5','g2-g4','d7-d5'];
  counter = 0;
  constructor(private chessservice: ChessServiceService, private userService: UserServiceService,
              private toastr: ToastrService) { }

   ngOnInit() {



    this.chessservice.getMovesByID(localStorage.getItem('selectedID')!).subscribe(data=>{this.moves = data;

    for (let i = 0; i < this.moves.length; i++) {
      this.moves[i] = this.moves[i].slice(0, 2) + '-' + this.moves[i].slice(2);
    }

    this.chessservice.getFenByID(localStorage.getItem('selectedID')!).subscribe(data=>{this.fens=data;
    this.fen = this.fens[0];

    const config = {
      draggable: true,
      dropOffBoard: 'snapback',
      position: 'start',
      sparePieces: false,
      onDrop: this.onDrop,
    };

    this.board = Chessboard('board', config);
    this.board.position(this.fen);
    this.puzzle = new Chess();
    this.puzzle.load(this.fen);

    if (this.puzzle.get(this.moves[0].charAt(0) + this.moves[0].charAt(1)).color === 'w') {
      this.board.flip();
    }
    setTimeout(() => {
      this.board.move(this.moves[this.counter]);
      this.counter++;
    }, 1000);


  });});
   }
  onDrop = (source: string, target: string, piece: string, newPos: any, oldPos: any, orientation: string) => {
    if (this.moves[this.counter]!==(source + '-' + target)) {
      return 'snapback';
    }
    this.counter++;
    setTimeout(() => {
      this.board.move(this.moves[this.counter]);
      this.counter++;
      if(this.moves[this.counter]== null){
        this.toastr.success('Congratulations! You\'ve successfully solved the chess puzzle.');
        this.chessservice.increasePassed(localStorage.getItem('currentUser')!,localStorage.getItem('selectedID')!).subscribe();
      }
    }, 1000);

    return;}
}

