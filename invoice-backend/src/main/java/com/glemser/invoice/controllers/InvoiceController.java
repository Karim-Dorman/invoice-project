package com.glemser.invoice.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.glemser.invoice.entities.Invoice;
import com.glemser.invoice.services.InvoiceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invoices")
public class InvoiceController {
	
	private final InvoiceService invoiceService;
	
	@PostMapping()
	@CrossOrigin(origins = "http://localhost:4200")
	public Invoice generateInvoice(@RequestBody Invoice invoice) {
		
		String firebaseUid = (String) org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		return invoiceService.generateInvoice(invoice,firebaseUid);
	}
	
	@PutMapping("/{id}")
	@CrossOrigin(origins = "http://localhost:4200")
	public Invoice updateInvoice(@RequestBody Invoice invoice, @PathVariable Long id) {
		return invoiceService.updateInvoice(invoice, id);
	}
	
	@PostMapping("/{id}/email")
	@CrossOrigin(origins = "http://localhost:4200")
    public void emailInvoice(@PathVariable Long id) {
        invoiceService.emailInvoice(id);
    }

	
	@GetMapping()
	@CrossOrigin(origins = "http://localhost:4200")
	public List<Invoice> getAllInvoices(){
		String firebaseUid = (String) org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return invoiceService.getAllInvoices(firebaseUid);
	}
	
	@GetMapping("/{id}")
	@CrossOrigin(origins = "http://localhost:4200")
	public Invoice getInvoiceById(@PathVariable Long id) {
		return invoiceService.getInvoiceById(id);
	}
	
}
