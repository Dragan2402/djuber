import { Injectable } from '@angular/core';


@Injectable({
  providedIn: 'root'
})
export class HashService{

  // private clientRoleHashed:string = "50d7026e31bc614f02983439cd0e9d83857b237f703fc9f5463a2a371dcc61f8";
  // private adminRoleHashed:string = "835d6dc88b708bc646d6db82c853ef4182fabbd4a8de59c213f2b5ab3ae7d9be";
  // private driverRoleHashed:string ="d61d9b699f596666b85ab18baa578f881751c296bc24cf6787e5fa0ae6c4c863";

  hashString(input: string): string {
    var hash = 0,
    i, chr;
    if (input.length === 0) return hash.toString();
    for (i = 0; i < input.length; i++) {
      chr = input.charCodeAt(i);
      hash = ((hash << 5) - hash) + chr;
      hash |= 0; // Convert to 32bit integer
    }
    return hash.toString();
  }

  matchRoles(roleUnhashed:string, roleHashed:string):boolean{
    // if(roleUnhashed==="CLIENT"){
    //   return this.clientRoleHashed ===roleHashed;
    // }else if(roleUnhashed==="ADMIN"){
    //   return this.adminRoleHashed === roleHashed;
    // }else{
    //   return this.driverRoleHashed===roleHashed;
    // }
    return (this.hashString(roleUnhashed)===roleHashed);
  }
}

