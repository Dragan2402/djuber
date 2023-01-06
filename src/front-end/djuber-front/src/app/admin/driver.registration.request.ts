export interface DriverRegistrationRequest{
  email:string;
  password:string;
  confirmPassword:string;
  firstName:string;
  lastName:string;
  city:string;
  phoneNumber:string;
  carType:string;
  licensePlate:string;
  additionalServices:string[];
  picture?:string;
}
