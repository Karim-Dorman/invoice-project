import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Invoice } from '../models/invoice';
import { Observable } from 'rxjs';

const BASE_URL = 'http://localhost:8081';

@Injectable({
  providedIn: 'root'
})
export class InvoiceService {

  constructor(private httpClient: HttpClient) { }

  generateInvoice(invoice: Invoice): Observable<Invoice> {
    return this.httpClient.post<Invoice>(`${BASE_URL}/api/invoices`, invoice);
  }

  updateInvoice(invoice: Invoice, id: number): Observable<Invoice> {
    return this.httpClient.put<Invoice>(`${BASE_URL}/api/invoices/${id}`, invoice);
  }

  emailInvoice(invoiceId: number): Observable<void> {
    return this.httpClient.post<void>(
      `${BASE_URL}/api/invoices/${invoiceId}/email`,
      {}
    );
  }

  getAllInvoices(): Observable<Invoice[]>{
    return this.httpClient.get<Invoice[]>(`${BASE_URL}/api/invoices`);
  }
}
