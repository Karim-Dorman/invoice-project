import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Employee } from '../models/employee';
import { Observable } from 'rxjs';

const BASE_URL = 'http://localhost:8081';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  constructor(private httpClient: HttpClient) { }

  createProfile(employee: Employee): Observable<Employee> {
    return this.httpClient.post<Employee>(`${BASE_URL}/api/employees`, employee);
  }

  getMyProfile(): Observable<Employee> {
    return this.httpClient.get<Employee>(`${BASE_URL}/api/employees/me`);
  }

  updateMyProfile(employee: Employee): Observable<Employee> {
    return this.httpClient.put<Employee>(`${BASE_URL}/api/employees/me`, employee)
  }

  getEmployee(id: number): Observable<Employee>{
    return this.httpClient.get<Employee>(`${BASE_URL}/api/employees/${id}`);
  }
}
