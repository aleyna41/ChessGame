export class User {
  userId:number| undefined
  username: string | undefined
  firstName: string | undefined
  lastName: string | undefined
  birthDate: string | undefined
  email: string | undefined
  points: number | undefined
  encodedPassword: string | undefined
  profilePicture: string | undefined
  isPrivate:boolean | undefined
  authCode: string | undefined
  getUserName(){
    return this.username;
  }
  getFirstName(){
    return this.firstName;
  }
  getLastName(){
    return this.lastName;
  }

  setUserName(username:any){
    this.username = username;
  }

  setFirstName(firstname: any){
    this.firstName = firstname;
  }
  setLastName(lastname:any){
    this.lastName = lastname;
  }
  setAuthCode(inputCode:any){
    this.authCode = inputCode;
  }
  getAuthCode(){
    return this.authCode;
  }




}


