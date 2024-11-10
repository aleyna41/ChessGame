
import {Component, ElementRef, OnDestroy, OnInit, TemplateRef, ViewChild} from '@angular/core';


import {MatSidenavModule} from '@angular/material/sidenav';
import {UserServiceService} from "../../Service/user-service.service";
import {FormBuilder, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {GameService} from "../../Service/game.service";
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {MatRadioChange} from "@angular/material/radio";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.css'],

})

export class LobbyComponent implements OnInit,OnDestroy {
    @ViewChild('usernameLabel') usernameLabel!: ElementRef;
    @ViewChild('gamenameLabel') gamenameLabel!: ElementRef;
    loggedUser =localStorage.getItem('currentUser');
    test:string[] = ['test1', 'test2'];
    gameInvites: String[] | any;
    myGames:String[] | any;
    localGames:String[] | any;
    //fan: string | any;
    refreshInterval:any;
    selectedTime = 3;
  game1:any;
  difficulty:any;





constructor(
  private router: Router,
  private userService: UserServiceService,
  private builder: FormBuilder,
  private gameService: GameService,
  private dialog: MatDialog,
  private toastr: ToastrService

  ) {
}



  gameform = this.builder.group({
    username: this.builder.control('',
      Validators.compose([Validators.required, Validators.minLength(5)])),

    gamename:this.builder.control(
      '', Validators.required
      )

  });
  botgameform = this.builder.group({
    botgamename:this.builder.control(
      '', Validators.required
    )

  });
  ngOnInit() {
    this.refreshTable();
    this.refreshInterval = setInterval(()=>{
      this.refreshTable();
    }, 5000);
  }

  refreshTable(){
    if(this.loggedUser !== null){
      this.userService.getGameInvites(this.loggedUser).subscribe(data =>{this.gameInvites = data});
      this.gameService.getMyGames(this.loggedUser).subscribe(data => {this.myGames = data});

    }
  }

  ngOnDestroy() {
    clearInterval(this.refreshInterval);
  }


  createGame(){
    if(!this.gameform.valid){
      return
    }
    if(this.loggedUser !== null &&
      this.gameform.value.username !== null &&
      this.gameform.value.username !== undefined &&
      this.gameform.value.gamename !== null &&
      this.gameform.value.gamename !== undefined)
    {
      localStorage.setItem('enemy', this.gameform.value.username as string);
      localStorage.setItem('player', this.loggedUser as string);
      this.userService.startGameRequest(
        this.loggedUser,
        this.gameform.value.username,
        this.gameform.value.gamename,
        this.selectedTime).subscribe(
          (response) => {
              // Erfolgreiche Antwort
              this.toastr.success('Game request sent successfully!');
              console.log('Erfolg:', response);
          },
          (error) => {
              // Fehlerbehandlung
              this.toastr.error('Creating the game failed. Please check the username or choose another game-name.')
              console.error('HTTP error:', error);
          }
      );
      this.gameform.reset();
        this.showLabel(this.usernameLabel);
        this.showLabel(this.gamenameLabel);
    }
  }
  onTimeSelected(event: any) {
    this.selectedTime = event.target.value;
  }

  acceptInvite(index: number){
    const acceptedInvite = this.gameInvites[index];
    localStorage.setItem('enemy',this.loggedUser as string);
    this.gameService.getPlayersByGamename(acceptedInvite).subscribe(data=>{this.game1 =data;
      if(this.game1[0]!== this.loggedUser){
      localStorage.setItem('player',this.game1[0] as string );
    }else{
      localStorage.setItem('player',this.game1[1] as string);
    }
    });
    this.userService.acceptGameRequest(acceptedInvite).subscribe(
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

  denyInvite(index: number){
    const deniedInvite = this.gameInvites[index];
    this.userService.denyGameRequest(deniedInvite).subscribe(
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

  openGame(index: number) {
    const openGame = this.myGames[index];
    this.gameService.openGame(openGame).subscribe(data => {this.localGames = data;
      localStorage.setItem('fen', this.localGames[this.localGames.length-1] as string);
      localStorage.setItem('gameName', openGame as string);

      this.router.navigate(['/gameboard/' + openGame]);
      });
  }

  handleInput(event: any) {
      const input = event.target;
      const label = input.nextElementSibling; // Das nächste Geschwister-Element ist das Label

      if (input.value.trim() !== '') {
          label.style.display = 'none'; // Das Label ausblenden, wenn Text vorhanden ist
      } else {
          label.style.display = 'block'; // Das Label anzeigen, wenn das Textfeld leer ist
      }
  }
    showLabel(labelElement: ElementRef) {
        const label = labelElement.nativeElement as HTMLElement;
        label.style.display = 'block';
    }

  onRadioChange($event: MatRadioChange){

    if
    ( $event.value== '1') {
      this.difficulty = 1;
    } else if($event.value== '2'){
      this.difficulty = 2;
    }else{
      this.difficulty = 3;
    }
  }


  openDialog(template: TemplateRef<any>): void {
    this.gameService.existGameByPlayers(this.loggedUser!, 'Bot Yasir').subscribe(data=>{
      if(data === true){
        this.router.navigate([`/againstbot/${localStorage.getItem('botgamename')}`]);
      } else{
        const dialogConfig: MatDialogConfig = {
          width: '400px',
          disableClose: true,
        };

        this.dialog.open(template, dialogConfig);
      }
    })
  }
  createBotGame(){
    this.dialog.closeAll();
    if(!this.botgameform.valid){
      return;
    }
    localStorage.setItem('diff',this.difficulty);
    this.userService.startGameRequest( this.loggedUser!,'Bot Yasir',this.botgameform.value.botgamename!, this.selectedTime).subscribe(
      (response) => {
        // Erfolgreiche Antwort
       this.userService.acceptGameRequest(this.botgameform.value.botgamename!).subscribe()
        this.router.navigate([`/againstbot/${localStorage.getItem('botgamename')}`])
        console.log('Erfolg:', response);
      },
      (error) => {
        // Fehlerbehandlung
        console.error('HTTP error:', error);
      }

    );
    localStorage.setItem('botgamename',this.botgameform.value.botgamename!)
  }

  goBack(): void {
    // Schließe das Modal-Fenster
    this.dialog.closeAll();

    // Navigiere zurück zur lobby-Komponente
    this.router.navigate(['/lobby']);
  }

}
