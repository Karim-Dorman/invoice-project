import { Invoice } from "./invoice";

export class Employee {
  id?: number;
  name: string = '';
	email: string ='';
	phoneNumber: string = '';
	companyName: string = '';
	companyAddress: string = '';
	clientName: string = '';
  clientEmail: string = '';
  clientAddress: string = '';
	rate: number = 0;
	
	invoices?: Invoice[];
}
