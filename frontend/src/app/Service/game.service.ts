import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Message} from "../Model/message";
@Injectable({
  providedIn: 'root'
})
export class GameService{
  private apiurl = 'http://localhost:8080';
  constructor(private http: HttpClient) {
  }

  getTimer(gameName:string){
    return this.http.get<number[]>(this.apiurl + '/game/getFormattedTime/' + gameName);
  }

  getAllGames(){
    return this.http.get<String[]>(this.apiurl + '/game/getAllGames')
  }
  getStreams(){
    return this.http.get<String[]>(this.apiurl + '/game/getStreams')
  }
  startLivestream(gameName: string){
    return this.http.post(this.apiurl + '/game/startLivestream', gameName)
  }

  getMyGames(loggedUser: String){
    return this.http.get<String[]>(this.apiurl + '/game/getMyGames/' + loggedUser)
  }

  openGame(gameName: string){
    return this.http.get<string>(this.apiurl + '/game/getGameInfo/' + gameName)
  }
  existGameByPlayers(player1:string, player2:string){
    return this.http.get(this.apiurl + `/game/getGamesof/${player1}/${player2}`)
  }

  sendFenAndSan(gameName:string, fen: string, san: string){
    const requestbody = {
      gameName: gameName,
      sanString: san,
      fenString: fen

    }
    return this.http.post(this.apiurl + '/game/updateGame', requestbody)
  }

  getPlayersByGamename(gamename:string){
    return this.http.get<String[]>(this.apiurl + '/game/getPlayers/' + gamename )
  }

  getGameSans(gameId: number){
    return this.http.get<String[]>(this.apiurl + '/game/getGameSans/' + gameId)
  }

  switchTimer(gameName:string){
    return this.http.post(this.apiurl + '/game/switchTimer', gameName)
  }

  stopTimer(gameName:string){
  return this.http.post(this.apiurl + '/game/stopTimer', gameName)
 }

  startTimer(gameName:string){
    return this.http.post(this.apiurl + '/game/startTimer', gameName)
  }

  getWhite(gameId: number){
    return this.http.get<string>(this.apiurl + '/game/getWhite/' + gameId)
  }

  addPoints(userName:string){
    return this.http.post(this.apiurl + '/game/addPoints', userName)
  }

  removePoints(userName:string){
    return this.http.post(this.apiurl + '/game/removePoints', userName)
  }

  /* result-page */

  getWinnerAndElo(gameId: number){
    return this.http.get<{ [key: string]: number }>(this.apiurl + '/game/getWinnerAndElo/' + gameId);
  }

  getLooserAndElo(gameId: number) {
    return this.http.get<{ [key: string]: number }>(this.apiurl + '/game/getLooserAndElo/' + gameId);
  }

  getWhiteAndElo(gameId: number){
    return this.http.get<{ [key: string]: number }>(this.apiurl + '/game/getWhiteAndElo/' + gameId);
  }

  getBlackAndElo(gameId: number){
    return this.http.get<{ [key: string]: number }>(this.apiurl + '/game/getBlackAndElo/' + gameId);
  }

  getGameId(gameName:string){
    return this.http.get<number>(this.apiurl + '/game/getGameId/' + gameName)
  }

  calculateBestMoveForAssistant(gameName:string){
    return this.http.get(this.apiurl + '/calculateBestMoveForAssistant/' + gameName)
  }

  payWithPoint(loggedUser:string){
    return this.http.post(this.apiurl + '/payWithPoint', loggedUser)
  }
 /* calculateBestMoveForAssistant(fen:string){
      const encodedFen = encodeURIComponent(fen);
      console.log(encodedFen);

      const url = `${this.apiurl}/calculateBestMoveForAssistant/${encodedFen}`;
      console.log(url);

      return this.http.get(url);

  }*/


  calculateBestMove(gameName:string, depth:number){
    return this.http.get(this.apiurl + `/calculateBestMove/${gameName}/${depth}` )
  }

  initializeBot(){
    return this.http.post(this.apiurl + '/initializeBot' , '')
  }
  deactivateBot(){
    return this.http.post(this.apiurl + '/deactivateBot', '')
  }
  surrender(gameName:string, loggedUser:string, enemy:string){
    const requestbody= {
      gameName:gameName,
      receiverUserName: enemy,
      senderUserName: loggedUser
    }
    return this.http.post(this.apiurl + '/game/surrender', requestbody)
  }
  sendDrawRequest(gameName:string, loggedUser:string, enemy:string){
    const requestbody= {
      gameName:gameName,
      receiverUserName: enemy,
      senderUserName: loggedUser
    }
    return this.http.post(this.apiurl + '/game/sendDrawRequest', requestbody)
  }
  acceptDrawRequest(gameName:string){
    return this.http.post(this.apiurl + '/game/acceptDrawRequest', gameName)
  }
  denyDrawRequest(gameName:string){
    return this.http.post(this.apiurl + '/game/rejectDrawRequest',gameName)
  }
  isDrawRequested(gameName:string){
    return this.http.get(this.apiurl + '/game/isDrawRequested/' + gameName)
  }
  getResult(gameName:string){
    return this.http.get<string>(this.apiurl + '/game/getResult/' + gameName)
  }

}

