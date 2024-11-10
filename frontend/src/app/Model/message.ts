export class Message {
  content: string;
  sender: string;
  receiver: string;
  type: MessageType;
  //timestamp: String;
  seen: boolean;
  msgId: number;
  contentColor?: string;
  senderColor?: string;
  constructor(
    content: string,
    sender: string,
    receiver: string,
    type: MessageType,
    seen: boolean,
    msgId: number
    //timestamp: String
  ) {
    this.content = content;
    this.sender = sender;
    this.receiver = receiver;
    this.type = type;
    this.seen = seen;
    this.msgId = msgId;
    //this.timestamp = timestamp;
  }
}

export enum MessageType {
  CHAT = 'CHAT',
  LEAVE = 'LEAVE',
  JOIN = 'JOIN'
}
