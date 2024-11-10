import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {PgnInfo} from "../Model/pgnInfo";

@Injectable({
  providedIn: 'root'
})
export class ExpImpServiceService {
  constructor(private http: HttpClient) { }

  private apiurl = 'http://localhost:8080';

  /*---------------------------------------- Import ----------------------------------------*/

  importPgn(pgn: File) {
    const formData: FormData = new FormData();
    formData.append('pgnFile', pgn, pgn.name);
    return this.http.post(`${this.apiurl}/uploadPgn`, formData);
  }

  /*---------------------------------------- Export ----------------------------------------*/

  // Pgn exportieren
  exportPgn(gameId: number) {
    return this.http.get<string>(`${this.apiurl}/saveManualFile/`+ gameId);
  }

  exportPgn2(fileCode: string) {
    return this.http.get(`${this.apiurl}${fileCode}`, { responseType: 'blob' });
  }
}
