import { Component, inject } from '@angular/core';
import { EmployeeService } from '../../services/employee.service';
import { Employee } from '../../models/employee';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, ɵInternalFormsSharedModule } from '@angular/forms';
import { InvoiceService } from '../../services/invoice.service';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../auth/auth.service';
import { Invoice } from '../../models/invoice';


@Component({
  selector: 'app-invoicesdashboard',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './invoicesdashboard.component.html',
  styleUrl: './invoicesdashboard.component.css'
})
export class InvoicesdashboardComponent {

  private authService = inject(AuthService);

  invoiceForm!: FormGroup;

  employee?: Employee;
  invoices: Invoice[] = [];
  errorMessage: string = '';
  isInvoiceProcessing: boolean = false;
  isLoggedIn: boolean = false;
  isEditMode: boolean = false;
  editingInvoiceId: number | null = null; 

  constructor(private employeeService: EmployeeService, private fb: FormBuilder, private invoiceService: InvoiceService, private router: Router, private activateRoute: ActivatedRoute) { }


  ngOnInit() {

    this.invoiceForm = this.fb.group({
      periodCovered: ['', Validators.required ],
      hoursWorked: ['', Validators.required ],
      description: ['', Validators.required ]
    })


    this.loadProfile();
    this.getAllInvoices();
  }

  get amount() {
    return (this.employee?.rate || 0) * this.invoiceForm.get('hoursWorked')?.value;
  }


  loadProfile() {

    this.employeeService.getMyProfile().subscribe(response => {
      this.employee = response;
    });
  }

  getAllInvoices() {
    this.invoiceService.getAllInvoices().subscribe({
      next: (response) => {
        this.invoices = response;
      },
      error: (err) => {
        this.errorMessage = 'Unable to load invoices. Please try again.';
      }
    })
  }

  onEdit(invoice: Invoice) {
    this.isEditMode = true;
    this.editingInvoiceId = invoice.id!;

    this.invoiceForm.patchValue({
      periodCovered: invoice.periodCovered,
      hoursWorked: invoice.hoursWorked,
      description: invoice.description,
    })

    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  onSubmit() {
  
    this.isInvoiceProcessing = true;

    if (this.isEditMode && this.editingInvoiceId != null) {
      this.invoiceService.updateInvoice(this.invoiceForm.value, this.editingInvoiceId).subscribe({
        next: (response) => {
          this.invoiceService.emailInvoice(response.id!).subscribe({
            next: () => {
              this.isInvoiceProcessing = false;
              this.getAllInvoices();
              this.invoiceForm.reset();
            },
            error: () => {
              this.isInvoiceProcessing = false;
              alert('Invoice updated, but email failed.');
            }
          })
        },
        error: () => {
          this.isInvoiceProcessing = false;
          alert('Failed to update invoice.');
        }
      })
    } else {
      this.invoiceService.generateInvoice(this.invoiceForm.value)
      .subscribe({
        next: (savedInvoice) => {
          this.invoiceService.emailInvoice(savedInvoice.id!)
            .subscribe({
              next: () => {
                this.isInvoiceProcessing = false;
                this.getAllInvoices();
                this.invoiceForm.reset();
              },
              error: () => {
                this.isInvoiceProcessing = false;
                alert('Invoice created, but email failed.');
              }
            });
        },
        error: () => {
          this.isInvoiceProcessing = false;
          alert('Failed to create invoice.');
        }
      });
    }

  }
  



}
