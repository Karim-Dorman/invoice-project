package com.glemser.invoice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.glemser.invoice.entities.Employee;
import com.glemser.invoice.entities.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
	List<Invoice> findByEmployee(Employee employee);
}
