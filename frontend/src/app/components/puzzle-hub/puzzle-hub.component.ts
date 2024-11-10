import {AfterViewInit, Component, OnInit} from '@angular/core';
import {ChessServiceService} from "../../Service/chess-service.service";
import {Router} from "@angular/router";
declare var Chessboard :any;
@Component({
  selector: 'app-puzzle-hub',
  templateUrl: './puzzle-hub.component.html',
  styleUrls: ['./puzzle-hub.component.css']
})
export class PuzzleHubComponent implements OnInit{
  puzzles: String[] = [];
  themes:String[] = [];

  constructor(private chessService:ChessServiceService,
              private router: Router) {
  }
  ngOnInit(){

    this.chessService.getAllIDs().subscribe(data =>{this.puzzles = data});
    this.chessService.getAllThemes().subscribe(data =>{this.themes = data});
  }
  setChessPuzzleData(){
    const inputElement = document.getElementById("csv") as HTMLInputElement;
    const selectedElement = inputElement.files;
    if(selectedElement){
      const selectedFile = selectedElement[0] as File ;
      this.chessService.setChessPuzzleData(selectedFile!).subscribe(
        (response) => {
          // Erfolgreiche Antwort
          this.chessService.getAllIDs().subscribe(data =>{this.puzzles = data});
          this.chessService.getAllThemes().subscribe(data =>{this.themes = data});
          console.log('Erfolg:', response);
        },
        (error) => {
          // Fehlerbehandlung
          console.error('HTTP error:', error);
        }
      );
    }
  }
  gotoPuzzle(i:number){
    localStorage.setItem("selectedPuzzle", this.themes[i] as string);
    localStorage.setItem("selectedID", this.puzzles[i] as string);
    this.router.navigate(['/chesspuzzle']);
  }
}
