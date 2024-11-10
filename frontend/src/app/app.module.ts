import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { FriendlistComponent } from './components/friendlist/friendlist.component';
import { SignupComponent } from './components/signup/signup.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from 'src/material.module';
import { HttpClientModule } from '@angular/common/http';
import { ToastrModule } from 'ngx-toastr';
import { HomepageComponent } from './components/homepage/homepage.component';
import {MatTableModule} from "@angular/material/table";
import { LobbyComponent } from './components/lobby/lobby.component';
import {MatSidenavModule} from "@angular/material/sidenav";
import { AuthComponent } from './components/auth/auth.component';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatNativeDateModule} from '@angular/material/core';
import { ProfileComponent } from './components/profile/profile.component';
import {MatGridListModule} from "@angular/material/grid-list";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";
import {MatDialogModule} from "@angular/material/dialog";
import {MatSelectModule} from "@angular/material/select";
import {ChatComponent} from "./components/chat/chat.component";
import {MatIconModule} from "@angular/material/icon";
import {ChatlistComponent} from "./components/chatlist/chatlist.component";
import { LeaderboardComponent } from './components/leaderboard/leaderboard.component';
import { ChessclubComponent } from './components/chessclub/chessclub.component';
import { GameboardComponent } from './components/gameboard/gameboard.component';
import { ClubChatComponent } from './components/club-chat/club-chat.component';
import { ChesspuzzleComponent } from './components/chesspuzzle/chesspuzzle.component';
import { PuzzleHubComponent } from './components/puzzle-hub/puzzle-hub.component';
import { StreamlistComponent } from './components/streamlist/streamlist.component';
import { RepeatGameComponent } from './components/repeat-game/repeat-game.component';
import { LivestreamComponent } from './components/livestream/livestream.component';
import { ExpImpComponent } from './components/exp-imp/exp-imp.component';
import { GameResultComponent } from './components/game-result/game-result.component';
import { DrawResultComponent} from "./components/draw-result/draw-result.component";
import { RepeatPgnGameComponent } from './components/repeat-pgn-game/repeat-pgn-game.component';
import { AgainstbotComponent} from "./components/againstbot/againstbot.component";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    FriendlistComponent,
    SignupComponent,
    HomepageComponent,
    LobbyComponent,
    AuthComponent,
    ProfileComponent,
    ChatComponent,
    ChatlistComponent,
    LeaderboardComponent,
    ChessclubComponent,
    ClubChatComponent,
      GameboardComponent,
      ChesspuzzleComponent,
      PuzzleHubComponent,
      StreamlistComponent,
      RepeatGameComponent,
      LivestreamComponent,
      ExpImpComponent,
      GameResultComponent,
      DrawResultComponent,
      RepeatPgnGameComponent,
      AgainstbotComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        BrowserAnimationsModule,
        MaterialModule,
        ReactiveFormsModule,
        HttpClientModule,
        ToastrModule.forRoot(),
        MatTableModule,
        MatSidenavModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatGridListModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        MatDialogModule,
        MatSelectModule,
        MatIconModule
    ],
  providers: [
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
