import { Employee } from "./employee";

export class Invoice {
  id?: number;
	invoiceNumber: string = '';
	periodCovered?: Date;
	description: string = '';
	hoursWorked: number = 0;
	amount: number = 0;
	
	employee?: Employee;
}
