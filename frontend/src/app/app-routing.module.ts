import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SignupComponent } from './components/signup/signup.component';
import { LoginComponent } from './components/login/login.component';
import { FriendlistComponent } from './components/friendlist/friendlist.component';
import { HomepageComponent } from './components/homepage/homepage.component';
import {LobbyComponent} from "./components/lobby/lobby.component";
import { AuthComponent } from './components/auth/auth.component';
import { ProfileComponent } from './components/profile/profile.component';
import {ChatComponent} from "./components/chat/chat.component";
import {ChatlistComponent} from "./components/chatlist/chatlist.component";
import {LeaderboardComponent} from "./components/leaderboard/leaderboard.component";
import {ChessclubComponent} from "./components/chessclub/chessclub.component";
import {GameboardComponent} from "./components/gameboard/gameboard.component";
import {ClubChatComponent} from "./components/club-chat/club-chat.component";
import {ChesspuzzleComponent} from "./components/chesspuzzle/chesspuzzle.component";
import {PuzzleHubComponent} from "./components/puzzle-hub/puzzle-hub.component";
import {StreamlistComponent} from "./components/streamlist/streamlist.component";
import {RepeatGameComponent} from "./components/repeat-game/repeat-game.component";
import {LivestreamComponent} from "./components/livestream/livestream.component";
import {ExpImpComponent} from "./components/exp-imp/exp-imp.component";
import {GameResultComponent} from "./components/game-result/game-result.component";
import {DrawResultComponent} from "./components/draw-result/draw-result.component";
import {RepeatPgnGameComponent} from "./components/repeat-pgn-game/repeat-pgn-game.component";
import {AgainstbotComponent} from "./components/againstbot/againstbot.component";

const routes: Routes = [
  {path:'',component:HomepageComponent},
  {path:'homepage',component:HomepageComponent},
  {path:'signup',component:SignupComponent},
  {path:'login',component:LoginComponent},
  {path:'friendlist',component:FriendlistComponent},
  {path: 'lobby', component:LobbyComponent},
  {path: 'auth', component:AuthComponent},
  {path: 'profile/:username', component: ProfileComponent },
  {path: 'private-chat/:loggedUser/:chatName', component: ChatComponent},
  {path: 'group-chat/:loggedUser/:chatName', component: ChatComponent},
  {path: 'chatlist', component: ChatlistComponent},
  {path: 'leaderboard', component: LeaderboardComponent},
  {path: 'chessclub', component: ChessclubComponent},
  {path: 'gameboard/:openGame', component: GameboardComponent},
  {path: 'clubchat/:loggedUser/:clubName', component: ClubChatComponent},
  {path: 'chesspuzzle', component: ChesspuzzleComponent},
  {path: 'puzzle-hub', component: PuzzleHubComponent},
  {path: 'streamlist', component: StreamlistComponent},
  {path: 'repeat-game/:profileUser/:enemy/:gameId', component: RepeatGameComponent},
  {path: 'livestream/:gameId', component: LivestreamComponent},
  {path: 'exp-imp', component: ExpImpComponent},
  {path: 'game-result/:gameId', component: GameResultComponent},
  {path: 'game-result/draw/:gameId', component: DrawResultComponent},
  {path: 'repeat-game/pgn/:i', component: RepeatPgnGameComponent},
  {path: 'againstbot/:gameId' , component: AgainstbotComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
