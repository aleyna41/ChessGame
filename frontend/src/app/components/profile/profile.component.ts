import {Component, OnDestroy, OnInit, TemplateRef} from '@angular/core';
import {UserServiceService} from "../../Service/user-service.service";
import {User} from "../../Model/user";
import {FriendServiceService} from "../../Service/friend-service.service";
import {chatService} from "../../Service/chat-service.service";
import {gamehistory} from "../../Model/gamehistory";
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {ChessServiceService} from "../../Service/chess-service.service";
import {Router} from "@angular/router";
import {Location} from "@angular/common";
import {ExpImpServiceService} from "../../Service/exp-imp-service.service";
import {saveAs} from "file-saver";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})


export class ProfileComponent implements OnInit,OnDestroy {
  user:User;
  friends: String[] = [];
  myChessClubs: String[] = [];
  gameHistory: gamehistory[] = [];
  anerkennung: String[] = [];
  private intervalId: any;
  isPrivate:any;
  profilePicture:string='';
  loggedUser =localStorage.getItem('currentUser' as string);
  friendPointer =localStorage.getItem('friendPointer' as string);
  profilePictureUrl: any;

  constructor(private userService: UserServiceService, private friendService: FriendServiceService,
              private chatService: chatService, private dialog: MatDialog, private chessService:ChessServiceService,
              private router: Router, private location: Location, private  expImpService: ExpImpServiceService,
              private toastr: ToastrService) {
    this.user = new User();
  }
  ngOnInit() {
    if (this.friendPointer !== null){
        if(this.loggedUser)
          this.userService.getProfilePicture(this.friendPointer).subscribe(
            (response: string) => {
              this.profilePicture = response;
            },
            (error) => {
              console.error('Fehler beim Laden des Profilbild-Pfads:', error);
            }
          );
      this.chatService.getMyChessClubs(this.friendPointer).subscribe(data =>{this.myChessClubs = data});
      this.userService.isPrivate(this.friendPointer).subscribe(data =>{this.isPrivate = data});
      this.userService.GetUserByName(this.friendPointer).subscribe(data =>{this.user = data});
      this.chessService.getAnerkennung(this.friendPointer).subscribe(data=>{this.anerkennung = data});
      this.refreshTables();
      this.intervalId = setInterval(()=>{
        this.refreshTables();
      }, 5000);
    }
  }
  refreshTables(){
    if(this.friendPointer === null || this.loggedUser === null){
      return;
    }
    if(this.loggedUser === this.friendPointer){
      this.friendService.GetFriends(this.loggedUser).subscribe(data =>{this.friends=data});
    }
    else{
      if(!this.isPrivate){

        this.friendService.GetFriends(this.friendPointer).subscribe(data =>{this.friends=data});
      }
      else{
      this.friends = ['Friends: private'];
      }
    }
    this.userService.getProfilePicture(this.friendPointer).subscribe(
      (response: string) => {
        this.profilePicture = response;
      },
      (error) => {
        console.error('Fehler beim Laden des Profilbild-Pfads:', error);
      }
    );
    this.userService.getGameHistory(this.friendPointer).subscribe(
      (response: gamehistory[]) => {
        this.gameHistory = response;
      },
      (error) => {
        console.error('Fehler beim Laden der Gamehistory:', error);
      }
    );
  }

  setProfilePicture() {
      const inputElement = document.getElementById('image') as HTMLInputElement;
      const selectedElement = inputElement.files;

      // Überprüfe, ob selectedElement nicht null oder undefined ist
      if (selectedElement) {
          // Stelle sicher, dass selectedElement vom Typ File | null ist
          const selectedFile = selectedElement[0] as File | null;

          if (selectedFile) {
              this.userService.setProfilePicture(this.loggedUser!, selectedFile).subscribe(
                  (response) => {
                      // Erfolgreiche Antwort
                      console.log('Erfolg:', response);
                  },
                  (error) => {
                      // Fehlerbehandlung
                      this.toastr.warning('Something went wrong while setting your profile picture. Please try again.');
                      console.error('HTTP error:', error);
                  }
              );
          }
      }
  }

  ngOnDestroy(){
      clearInterval(this.intervalId);
      localStorage.setItem('friendPointer', this.loggedUser!);
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

    repeatGame(item: gamehistory){
    this.closeModal();
    localStorage.setItem('result', item.result);
    const currentPath = this.location.path();
    localStorage.setItem('path', currentPath);
    this.router.navigate(['/repeat-game', item.user, item.enemy, item.gameId]);
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

  blackPoints(item: gamehistory):number{
  if (item.black === item.user){
    return item.userPoints;
    } else return item.enemyPoints;
  }

  whitePoints(item: gamehistory):number{
    if (item.white === item.user){
      return item.userPoints;
    } else return item.enemyPoints;
  }
}

