import {Component, HostListener, OnDestroy, OnInit, TemplateRef} from '@angular/core';
import {gamehistory} from "../../Model/gamehistory";
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {UserServiceService} from "../../Service/user-service.service";
import {Router} from "@angular/router";
import {ExpImpServiceService} from "../../Service/exp-imp-service.service";
import {PgnInfo} from "../../Model/pgnInfo";
import {Location} from '@angular/common';
import { saveAs } from 'file-saver';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-exp-imp',
  templateUrl: './exp-imp.component.html',
  styleUrls: ['./exp-imp.component.css']
})
export class ExpImpComponent implements OnInit,OnDestroy {
  loggedUser = localStorage.getItem('currentUser' as string);
  gameHistory: gamehistory[] = [];
  intervalId: any;
  pgnSans: Object = [];
  pgns: File[] = [];


  constructor(private userService: UserServiceService, private dialog: MatDialog,
              private router: Router, private expImpService: ExpImpServiceService,
              private location: Location, private toastr: ToastrService) {
  }

  ngOnInit() {
    this.refreshTables();
    this.intervalId = setInterval(() => {
      this.refreshTables();
    }, 5000);
  }

  ngOnDestroy() {
    clearInterval(this.intervalId);
  }

  refreshTables() {
    if (this.loggedUser)
      this.userService.getGameHistory(this.loggedUser).subscribe(
        (response: gamehistory[]) => {
          this.gameHistory = response;
        },
        (error) => {
          console.error('Fehler beim Laden der Gamehistory:', error);
        }
      );
  }

  openModal(template: TemplateRef<any>) {
    const dialogConfig: MatDialogConfig = {
      width: '500px',
      disableClose: true,
    };
    this.dialog.open(template, dialogConfig);
  }

  closeModal() {
    this.dialog.closeAll();
  }

  onFileSelected(event: any): void {
    const file: File = event.target.files[0];
    console.log('Selected file:', file);
  }

  repeatGame(item: gamehistory) {
    this.closeModal();
    const currentPath = this.location.path();
    localStorage.setItem('path', currentPath);
    this.router.navigate(['/repeat-game', item.user, item.enemy, item.gameId]);
  }

  importPgn() {
    const inputElement = document.getElementById('pgnFileInput') as HTMLInputElement;
    const selectedFiles = inputElement.files;

    if (selectedFiles) {
      const selectedPgnFile = selectedFiles[0] as File | null;

      if (selectedPgnFile) {
        this.expImpService.importPgn(selectedPgnFile).subscribe(
          (response) => {
            // Erfolgreiche Antwort
            this.pgns.push(selectedPgnFile);
            localStorage.setItem('name', selectedPgnFile.name);
            console.log('Erfolg:', response);
          },
          (error) => {
            // Fehlerbehandlung
            this.toastr.error('Import failed. Please try again later!');
            console.error('HTTP error:', error);
          }
        );
      }
    }
  }

  repeatImportGame(item: File, i: number) {
    this.expImpService.importPgn(item).subscribe(
      (response) => {
        // Erfolgreiche Antwort
        this.pgnSans = response;
        console.log(JSON.stringify(response));
        localStorage.setItem('pgnSans', JSON.stringify(response));
        console.log('Erfolg:', response);
      },
      (error) => {
        // Fehlerbehandlung
        this.toastr.error('San import failed. Please check the imported PGN for correctness.');
        console.error('HTTP error:', error);
      }
    );
    const currentPath = this.location.path();
    localStorage.setItem('path', currentPath);
    this.router.navigate(['repeat-game/pgn/', i]);
  }

  exportPgn(gameId: number) {
    this.expImpService.exportPgn(gameId).subscribe(
      (response: string) => {
        // Erfolgreiche Antwort
        this.expImpService.exportPgn2(response).subscribe(
          (response: any) =>{
            saveAs(response, 'SchachEP_game_' + gameId + '.pgn');
          },
          (error)=>{
            // Fehlerbehandlung
            console.error('Hier', error);
          }
        )
      },
      (error) => {
        // Fehlerbehandlung
        console.error('Fehler beim Exportieren der PGN:', error);
      }
    );
  }
}
