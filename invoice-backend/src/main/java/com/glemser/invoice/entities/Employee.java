package com.glemser.invoice.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String firebaseUid;
	private String name;
	private String email;
	private String phoneNumber;
	private String companyName;
	private String companyAddress;
	private String clientName;
	private String clientEmail;
	private String clientAddress;
	private double rate;
	
	@OneToMany(mappedBy = "employee")
	@JsonIgnore
	private List<Invoice> invoices;
	
}
