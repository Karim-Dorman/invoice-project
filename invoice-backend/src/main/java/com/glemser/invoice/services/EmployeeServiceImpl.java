package com.glemser.invoice.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.glemser.invoice.entities.Employee;
import com.glemser.invoice.repositories.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
	
	private final EmployeeRepository employeeRepository;
	
	

	@Override
	public Employee getCurrentEmployee(String firebaseUid) {
		return employeeRepository.findByFirebaseUid(firebaseUid);
	}

	@Override
	public Employee addEmployee(Employee employee, String firebaseUid) {
		employee.setFirebaseUid(firebaseUid);
		return employeeRepository.save(employee);
	}

	@Override
	public Employee getEmployeeById(Long id) {
		return employeeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
	}

	@Override
	public Employee updateProfile(Employee employee, String firebaseUid) {
		
		Employee existingProfile = getCurrentEmployee(firebaseUid);
		existingProfile.setName(employee.getName());
		existingProfile.setEmail(employee.getEmail());
		existingProfile.setPhoneNumber(employee.getPhoneNumber());
		existingProfile.setRate(employee.getRate());
		existingProfile.setCompanyName(employee.getCompanyName());
		existingProfile.setCompanyAddress(employee.getCompanyAddress());
		existingProfile.setClientName(employee.getClientName());
		existingProfile.setClientEmail(employee.getClientEmail());
		existingProfile.setClientAddress(employee.getClientAddress());
		return employeeRepository.save(existingProfile);
	}	
	
}
