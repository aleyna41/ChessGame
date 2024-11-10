import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root',

  })

export class globals{
  get loggedUser(): string {
    return this._loggedUser;
  }

  set loggedUser(value: string) {
    this._loggedUser = value;
  }
  private _loggedUser: string;

  constructor() {
    this._loggedUser = "";

  }

}
