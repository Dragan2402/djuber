export interface Driver{
  id: number,
  identityId: number,
  email: string,
  firstName: string,
  lastName: string,
  city: string,
  phoneNumber: string,
  carId : number,
  carType: string,
  licensePlate:string,
  isBlocked:boolean,
  additionalService:string[]
}
