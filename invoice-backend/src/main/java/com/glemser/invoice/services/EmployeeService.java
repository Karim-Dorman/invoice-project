package com.glemser.invoice.services;

import com.glemser.invoice.entities.Employee;

public interface EmployeeService {

	Employee addEmployee(Employee employee, String firebaseUid);

	Employee getCurrentEmployee(String firebaseUid);

	Employee getEmployeeById(Long id);

	Employee updateProfile(Employee employee, String firebaseUid);

}
