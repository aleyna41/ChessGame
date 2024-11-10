import {Component, OnDestroy, OnInit} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { chatService} from "../../Service/chat-service.service";
import {MessageType} from "../../Model/message";
import {Message} from "../../Model/message";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnDestroy {
  loggedUser = localStorage.getItem('currentUser' as string);
  chatName: string = '';
  chatHistory: Message[] = [];
  newMessage: "";
  chatType: string = '';
  editingMessage: Message | null = null;
  origContent: String = '';
  origMsg: Message | null = null;
  refreshInterval: any;

  constructor(private route: ActivatedRoute, private chatService: chatService) {
    this.route.params.subscribe(params => {
      this.chatName = params['chatName'];
    })
    this.newMessage = this.route.snapshot.params['message'];
  }

  ngOnInit(): void {
    // Überwache Änderungen in der URL
    this.route.url.subscribe(urlSegments => {
      // Segment aus URL extrahieren
      const lastSegment = urlSegments[urlSegments.length - 3].path;
      // Chat-Typ auf Url-Typ setzen
      this.chatType = lastSegment === 'group-chat' ? 'group' : 'private';
    });
    this.route.params.subscribe(params => {
      this.chatName = params['chatName'];
      this.getChatHistory();
    });
    this.refreshInterval = setInterval(() => {
      // Überprüfe, ob der Benutzer gerade eine Nachricht bearbeitet
      if (!this.editingMessage) {
        this.route.params.subscribe(params => {
          this.chatName = params['chatName'];
          this.getChatHistory();
        });
      }
    }, 1000);
  }

  ngOnDestroy() {
  }

  getChatHistory(): void {
    if (this.loggedUser !== null) {
      this.chatService.getChatHistory(this.loggedUser, this.chatName, this.chatType)
          .subscribe((history: Message[]) => {
            for (const message of history) {
              if(!message.seen){
                if(this.loggedUser)
                this.chatService.markMessagesAsSeen(this.loggedUser, message)
                  .subscribe((response) => {
                      // Erfolgreiche Antwort
                      message.seen = true;
                      console.log('Erfolg:', response);
                    },
                    (error) => {
                      // Fehlerbehandlung
                      console.error('HTTP error:', error);
                    });
              }
              if (message.sender === this.loggedUser) {
                message.senderColor = '#0FB27C';
              } else if (message.sender === 'System') {
                message.senderColor = '#2080bd';
              } else {
                message.senderColor = '#0FB27C';
              }
              message.contentColor = '#eeeeee';
            }
            this.chatHistory = history;
          });
    }
  }


  sendMessage(): void {
    if (this.loggedUser) {
      const message: Message = {
        content: this.newMessage,
        sender: this.loggedUser,
        receiver: this.chatName,
        type: MessageType.CHAT,
        seen: false,
        msgId: 0
      };
      if(this.chatType === 'private') {
        this.chatService.sendPrivateChatMessage(message).subscribe((response) => {
              // Erfolgreiche Antwort
              console.log('Erfolg:', response);
            },
            (error) => {
              // Fehlerbehandlung
              console.error('HTTP error:', error);
            }
        );
      } else if (this.chatType === 'group'){
        this.chatService.sendGroupChatMessage(message).subscribe((response) => {
              // Erfolgreiche Antwort
              console.log('Erfolg:', response);
            },
            (error) => {
              // Fehlerbehandlung
              console.error('HTTP error:', error);
            }
        );
      }

      if (message.sender === this.loggedUser) {
        message.senderColor = '#0FB27C';
      } else if (message.sender === 'System') {
        message.senderColor = '#2080bd';
      } else {
        message.senderColor = '#0FB27C';
      }
      message.contentColor = '#eeeeee';

      // Füge die Nachricht zur Liste der Nachrichten hinzu
      this.chatHistory.push(message);

      // Leere das Eingabefeld
      this.newMessage = '';
    }
  }

  editMessage(message: Message) {
    this.origMsg = message;
    this.origContent = message.content;
    this.editingMessage = message;
  }

  submitEdit(){
    if(this.editingMessage == null || this.isWhitespace(this.editingMessage.content)){
      if (this.origMsg)
      this.chatService.deleteMessage(this.origMsg).subscribe((response) => {
          // Erfolgreiche Antwort
          console.log('Erfolg:', response);
        },
        (error) => {
          // Fehlerbehandlung
          console.error('HTTP error:', error);
        });
    }
    else if (this.origContent === this.editingMessage.content ||
      this.origContent === this.editingMessage.content.trim()){
    } else {
      if(this.origMsg)
      this.chatService.editMessage(this.editingMessage.content, this.origMsg.msgId).subscribe((response) => {
          // Erfolgreiche Antwort
          console.log('Erfolg:', response);
        },
        (error) => {
          // Fehlerbehandlung
          console.error('HTTP error:', error);
        });
    }
    this.origMsg = null;
    this.editingMessage = null;
    this.origContent = '';
  }

  isWhitespace(message: string): boolean {
    const trimmedStr = message.trim();
    return trimmedStr.length === 0;
  }
}


