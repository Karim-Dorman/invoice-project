import { Injectable, inject } from '@angular/core';
import { Auth, EmailAuthProvider, createUserWithEmailAndPassword, reauthenticateWithCredential, signInWithEmailAndPassword, signOut, updatePassword, user } from '@angular/fire/auth';
import { from, map } from 'rxjs';
import { EmployeeService } from '../services/employee.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private auth = inject(Auth);
  
  currentUser$ = user(this.auth);
  isLoggedIn$ = this.currentUser$.pipe(map(user => user !== null));

  register(email: string, password: string) {
    return from(createUserWithEmailAndPassword(this.auth, email, password));
  }

  login(email: string, password: string) {
    return from(signInWithEmailAndPassword(this.auth, email, password));
  }

  logout() {
    return signOut(this.auth);
  }

  async changePassword(currentPassword: string, newPassword: string): Promise<void>{
    const currentUser = this.auth.currentUser;

    if (!currentUser || !currentUser.email) {
      throw new Error('No logged-in user found.')
    }

    const credential = EmailAuthProvider.credential(currentUser.email, currentPassword);

    await reauthenticateWithCredential(currentUser, credential);
    await updatePassword(currentUser, newPassword);

  }

}


