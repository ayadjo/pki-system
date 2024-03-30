import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../infrastructure/auth/model/user.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  currentUser!:any;

  constructor(private http: HttpClient) { }

  getMyInfo(mail: string): void {
    this.http.get<User>('http://localhost:8080/user/getByEmail/' + mail)
      .subscribe(
        user => {
          console.log('Received User:', user);        
          this.currentUser = user;
          localStorage.setItem('user',JSON.stringify(this.currentUser));
        },
        error => {
          console.error('Error fetching user info:', error);
        }
      );
  }

  getUserDetails(mail: string): Observable<User> {
    return this.http.get<User>(`http://localhost:8080/user/getByEmail/${mail}`);
  }

  createRootCertificate(startDate: Date, endDate: Date, userId: number) : Observable<any> {
    throw new Error('Method not implemented.');
  }
}
