import {Component, OnInit, TemplateRef} from '@angular/core';
import { chatService } from "../../Service/chat-service.service";
import { Router } from '@angular/router';
import {ToastrService} from "ngx-toastr";
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {ChatInfo} from "../../Model/chatInfo";
import {gamehistory} from "../../Model/gamehistory";
import {PgnInfo} from "../../Model/pgnInfo";


@Component({
  selector: 'app-chatlist',
  templateUrl: './chatlist.component.html',
  styleUrls: ['./chatlist.component.css']
})
export class ChatlistComponent implements OnInit {
  loggedUser =localStorage.getItem('currentUser' as string);
  friendName: string = '';
  privateChats: String[] = [];
  groupChats: String[] = [];
  chats: String[] = [];
  refreshInterval: any;
  chatInfo: ChatInfo[]=[];
  selectedFriends: String[] = [];
  selectedName: String = '';
  groupName: string = '';
  chatType: String[] = [];

  constructor(private chatService: chatService, private router: Router, private toastr: ToastrService,private dialog: MatDialog,
             /* private chatInfo: ChatInfo*/) { }

  ngOnInit(): void {
    this.refreshTables();
    this.refreshInterval = setInterval(()=>{
      this.refreshTables();
    }, 5000);
  }

  ngOnDestroy() {
    clearInterval(this.refreshInterval);
  }

  goToChat(i: number): void {
    const chat = this.chats[i];
    if (this.privateChats.includes(chat)){
      this.router.navigate(['/private-chat', this.loggedUser, chat]);
    } else {
      this.router.navigate(['/group-chat', this.loggedUser, chat]);
    }
  }

  deleteChat(i: number) {
    if(this.loggedUser)
    this.chatService.deleteChat(this.loggedUser, this.chats[i].toString(), this.chatType[i].toString()).subscribe(
      (response) => {
        // Erfolgreiche Antwort
        this.toastr.success('Yippie');
        /*this.router.navigate(['/private-chat', {chatName: this.friendName}]);*/
      },
      (error) => {
        // Fehlerbehandlung
        this.toastr.warning('Es geht nicht!')
        console.error('HTTP error:', error);
      }
    );
  }

  refreshTables() {
    if (this.loggedUser) {
      this.chatService.GetPrivateChats(this.loggedUser).subscribe(data => {
        this.chatInfo = [];
        this.chats = [];
        if(data.length!=0){
          const privatechats: String[] = [];
          data.forEach((chat: ChatInfo) => {
            if (!this.chats.includes(chat.friendName)) {
              this.chatType.push("private")
              this.chats.push(chat.friendName)
              privatechats.push(chat.friendName)
              this.chatInfo.push(chat)
            }
            this.privateChats = [];
            this.privateChats = privatechats;
          });
        }
      });
      this.chatService.GetGroupChats(this.loggedUser).subscribe(data => {
        if(data.length != 0){
          const groupchats: String[] = [];
          data.forEach((chat: ChatInfo) => {
            if(!this.chats.includes(chat.friendName)){
              this.chatType.push("group")
              this.chats.push(chat.friendName)
              groupchats.push(chat.friendName)
              this.chatInfo.push(chat)
            }
            this.groupChats=[];
            this.groupChats = groupchats;
          });
        }
      });
    }
  }

  // Privaten Chat erstellen
  createChat() {
    this.dialog.closeAll();
    if (this.loggedUser !== null) {
      if (this.friendName.trim() !== '') {
        this.chatService.createPrivateChat(this.loggedUser, this.friendName).subscribe(
            (chatInfo) => {
              // Erfolgreiche Antwort
              this.friendName = chatInfo.friendName;
              this.toastr.success('Yippie');
              console.log('Erfolg:', chatInfo);
              this.toastr.success('jaaaaa');
              /*this.router.navigate(['/private-chat', {chatName: this.friendName}]);*/
            },
            (error) => {
              // Fehlerbehandlung
              this.toastr.warning('Es geht nicht!')
              console.error('HTTP error:', error);
            }
        );
      } else {
        console.error('Der Freundesname darf nicht leer sein.');
      }
    }
  }

  openChatInfoModal(template: TemplateRef<any>): void {
    const dialogConfig: MatDialogConfig = {
      width: '400px',
      disableClose: true,
    };
    this.dialog.open(template, dialogConfig);

  }



  createGroupChat() {
    this.dialog.closeAll();
    if (this.loggedUser !== null) {
        this.chatService.createGroupChat(this.groupName, this.loggedUser, this.selectedFriends).subscribe(
            (chatInfo) => {
              // Erfolgreiche Antwort
              this.friendName = chatInfo.friendName;
              this.toastr.success('Yippie');
              console.log('Erfolg:', chatInfo);
              this.toastr.success('jaaaaa');
              /*this.router.navigate(['/private-chat', {chatName: this.friendName}]);*/
            },
            (error) => {
              // Fehlerbehandlung
              this.toastr.warning('Es geht nicht!')
              console.error('HTTP error:', error);
            }
        );
    }
  }

  openFriendSelectionModal(template: TemplateRef<any>): void {
    const dialogConfig: MatDialogConfig = {
      width: '400px',
      disableClose: true,
    };

    this.dialog.open(template, dialogConfig);
  }

  openGroupSelectionModal(grouptemplate: TemplateRef<any>): void {
    const dialogConfig: MatDialogConfig = {
      width: '400px',
      disableClose: true,
    };

    this.dialog.open(grouptemplate, dialogConfig);
  }

  goBack(): void {
    // Schließe das Modal-Fenster
    this.dialog.closeAll();

    // Navigiere zurück zur chatlist-Komponente
    this.router.navigate(['/chatlist']);
  }

  addSelectedFriend() {
    if (this.selectedName && !this.selectedFriends.includes(this.selectedName)) {
      this.selectedFriends.push(this.selectedName);
      this.selectedName = '';
    }
  }

  removeSelectedFriend(index: number) {
    this.selectedFriends.splice(index, 1);
  }

}
