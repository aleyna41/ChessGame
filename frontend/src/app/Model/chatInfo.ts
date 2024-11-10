export class ChatInfo {
  friendName: string;
  creationDate: Date;
  memberUsernames: String[];

  constructor(friendName: string, creationDate: Date, memberUsernames: String[]) {
  this.friendName = friendName;
  this.creationDate = creationDate;
  this.memberUsernames = memberUsernames;
  }
}
