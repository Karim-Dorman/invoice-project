package com.glemser.invoice.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.glemser.invoice.entities.Employee;
import com.glemser.invoice.entities.Invoice;
import com.glemser.invoice.repositories.EmployeeRepository;
import com.glemser.invoice.repositories.InvoiceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
	

	private static final String INVOICE_PREFIX = "INV-";
	
	private final InvoiceRepository invoiceRepository;
	private final EmployeeRepository employeeRepository;
	
	private final EmailService emailService;
	private final InvoicePdfService invoicePdfService;
	

	@Override
	public Invoice generateInvoice(Invoice invoice, String firebaseUid) {
		Employee emp = employeeRepository.findByFirebaseUid(firebaseUid);
		
		invoice.setEmployee(emp);
		invoice.setInvoiceNumber(this.generateRandomInvoiceNumber());
		
		double amount = invoice.getHoursWorked() * emp.getRate();
		invoice.setAmount(amount);
		
		return invoiceRepository.save(invoice);
	}

	@Override
	public Invoice updateInvoice(Invoice invoice, Long id) {
		Invoice existingInvoice = getInvoiceById(id);
		existingInvoice.setPeriodCovered(invoice.getPeriodCovered());
		existingInvoice.setHoursWorked(invoice.getHoursWorked());
		existingInvoice.setDescription(invoice.getDescription());
		return invoiceRepository.save(existingInvoice);
	}
	
	private String generateRandomInvoiceNumber() {
		int number = (int) (Math.random() * 9000) + 1000;
		return INVOICE_PREFIX + number;
	}
	
	public void emailInvoice(Long invoiceId) {

	    Invoice invoice = invoiceRepository.findById(invoiceId)
	            .orElseThrow(() -> new RuntimeException("Invoice not found"));

	    byte[] pdf = invoicePdfService.generateInvoicePdf(invoice);

	    String to = invoice.getEmployee().getClientEmail();
	    String subject = "Invoice " + invoice.getInvoiceNumber();
	    String body = "Hello,\n\nPlease find attached invoice.\n\nThanks,";

	    emailService.sendInvoicePdf(to, subject, body, pdf, "invoice-" + invoice.getInvoiceNumber() + ".pdf");
	}

	@Override
	public List<Invoice> getAllInvoices(String firebaseUid) {
		Employee emp = employeeRepository.findByFirebaseUid(firebaseUid);
		
		if(emp == null) {
			new RuntimeException("Employee not found for firebaseUid: " + firebaseUid);			
		}
		
		return invoiceRepository.findByEmployee(emp);	
	}

	@Override
	public Invoice getInvoiceById(Long id) {
		return invoiceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found"));
	}
	
	

}
