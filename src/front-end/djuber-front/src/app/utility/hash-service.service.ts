import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class HashService{

  private clientRoleHashed:string = "50d7026e31bc614f02983439cd0e9d83857b237f703fc9f5463a2a371dcc61f8";
  private adminRoleHashed:string = "835d6dc88b708bc646d6db82c853ef4182fabbd4a8de59c213f2b5ab3ae7d9be";
  private driverRoleHashed:string ="d61d9b699f596666b85ab18baa578f881751c296bc24cf6787e5fa0ae6c4c863";

  async hashString(input: string): Promise<string> {
    const hash = await window.crypto.subtle.digest('SHA-256', new TextEncoder().encode(input));
    return this.hex(hash);
  }

  // Converts an ArrayBuffer to a hexadecimal string
  private hex(buffer: ArrayBuffer): string {
    const hexCodes = [];
    const view = new DataView(buffer);
    for (let i = 0; i < view.byteLength; i += 4) {
      // Using getUint32 reduces the number of iterations needed (we process 4 bytes each time)
      const value = view.getUint32(i);
      // toString(16) will give the hex representation
      const stringValue = value.toString(16);
      // We use concatenation and slice for padding
      const padding = '00000000';
      const paddedValue = (padding + stringValue).slice(-padding.length);
      hexCodes.push(paddedValue);
    }

    // Join all the hex strings into one
    return hexCodes.join('');
  }

  matchRoles(roleUnhashed:string, roleHashed:string):boolean{
    if(roleUnhashed==="CLIENT"){
      return this.clientRoleHashed ===roleHashed;
    }else if(roleUnhashed==="ADMIN"){
      console.log(this.adminRoleHashed===roleHashed)
      return this.adminRoleHashed === roleHashed;
    }else{
      return this.driverRoleHashed===roleHashed;
    }
  }
}

