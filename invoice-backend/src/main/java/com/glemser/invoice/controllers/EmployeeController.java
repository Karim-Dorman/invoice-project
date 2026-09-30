package com.glemser.invoice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.glemser.invoice.entities.Employee;
import com.glemser.invoice.services.EmployeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {
	
	private final EmployeeService employeeService;
	
	@PostMapping()
	@CrossOrigin(origins = "http://localhost:4200")
	public Employee addEmployee(@RequestBody Employee employee) {
		String firebaseUid = (String) org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return employeeService.addEmployee(employee, firebaseUid);
	}
	
	@GetMapping("/me")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<Employee> getCurrentEmployee() {
		
		String firebaseUid = (String) org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Employee employee = employeeService.getCurrentEmployee(firebaseUid);

		if (employee == null) {
			return ResponseEntity.notFound().build(); 
		}

		return ResponseEntity.ok(employee);
	}
	
	@GetMapping("/{id}")
	@CrossOrigin(origins = "http://localhost:4200")
	public Employee getEmployeeById(@PathVariable Long id) {
		return employeeService.getEmployeeById(id);
	}
	
	@PutMapping("/me")
	@CrossOrigin(origins = "http://localhost:4200")
	public Employee updateProfile(@RequestBody Employee employee) {
		String firebaseUid = (String) org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return employeeService.updateProfile(employee, firebaseUid);
	}
}
