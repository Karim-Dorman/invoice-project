package com.glemser.invoice.services;

import java.util.List;

import com.glemser.invoice.entities.Invoice;

public interface InvoiceService {

	Invoice generateInvoice(Invoice invoice, String firebaseUid);
	void emailInvoice(Long invoiceId);
	List<Invoice> getAllInvoices(String firebaseUid);
	Invoice updateInvoice(Invoice invoice, Long id);
	Invoice getInvoiceById(Long id);

}
