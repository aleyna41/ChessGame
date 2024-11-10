import {Component, OnInit, TemplateRef} from '@angular/core';
import { chatService } from "../../Service/chat-service.service";
import { Router } from '@angular/router';
import {ToastrService} from "ngx-toastr";
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {ChatInfo} from "../../Model/chatInfo";
import {UserServiceService} from "../../Service/user-service.service";
import {User} from "../../Model/user";


@Component({
  selector: 'app-chessclub',
  templateUrl: './chessclub.component.html',
  styleUrls: ['./chessclub.component.css']
})
export class ChessclubComponent implements OnInit {
  loggedUser =localStorage.getItem('currentUser' as string);
  club: string = '';
  chessClubs: String[] = [];
  myChessClubs: String[] = [];
  user:User;
  private createdChatIds: number[] | undefined;

  constructor(private userService: UserServiceService,private chatService: chatService, private router: Router, private toastr: ToastrService,private dialog: MatDialog,
              /* private chatInfo: ChatInfo*/) { this.user = new User}

  ngOnInit(): void {
    this.chatService.getChessClubs().subscribe(data=>{this.chessClubs= data});
    this.chatService.getMyChessClubs(this.loggedUser!).subscribe(data =>{this.myChessClubs = data});
  }

  goToClubChat(i: number): void {
    const chat = this.chessClubs[i];
    this.router.navigate(['/clubchat', this.loggedUser, chat]);
  }

  joinChessClub(i: number) {
    const clubname = this.chessClubs[i];
    if(this.loggedUser !== null){
      this.chatService.joinChessClub(this.loggedUser, clubname).subscribe(
        (response) => {
          // Erfolgreiche Antwort
          this.chatService.getMyChessClubs(this.loggedUser!).subscribe(data=>{this.myChessClubs= data});
        },
        (error) => {
          // Fehlerbehandlung
          this.toastr.error('Joining the chess club failed.');
        }
      );
    }
  }

  // Chessclub mit clubchat erstellen
  createChessClub() {
    this.dialog.closeAll();
    this.userService.GetUserByName(this.loggedUser!).subscribe(data =>{this.user = data});
    this.chatService.createChessClub(this.loggedUser!,this.club).subscribe(
      (response) => {
        // Erfolgreiche Antwort
        this.toastr.success('Erfolg: '+ response);
        this.chatService.getChessClubs().subscribe(data=>{this.chessClubs= data});
        this.chatService.getMyChessClubs(this.loggedUser!).subscribe(data=>{this.myChessClubs= data});

      },
      (error) => {
        // Fehlerbehandlung
        this.toastr.warning('Club konnte nicht erstellt werden!');
        console.log(error.message());
      }
    );
  }

  openFriendSelectionModal(template: TemplateRef<any>): void {
    const dialogConfig: MatDialogConfig = {
      width: '400px',
      disableClose: true,
    };

    this.dialog.open(template, dialogConfig);
  }

  goBack(): void {
    // Schließe das Modal-Fenster
    this.dialog.closeAll();

    // Navigiere zurück zur chatlist-Komponente
    this.router.navigate(['/chessclub']);
  }
}
