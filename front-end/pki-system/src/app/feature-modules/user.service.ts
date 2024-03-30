import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../infrastructure/auth/model/user.model';
import { Observable } from 'rxjs';
import { CertificateType } from './certificates/model/certificateDto.model';
import { Certificate } from '../infrastructure/auth/model/certificate.model';

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

  createRootCertificate(startDate: Date, endDate: Date, issuerMail: string, filePass: string): Observable<any> {
    const requestBody = { startDate, endDate, issuerMail };
    return this.http.post<any>(`http://localhost:8080/certificates/root-certificate/${filePass}`, requestBody);
  }

  createCACertificate(issuerMail: string, subjectMail: string, issuerCertificateSerialNumber: string,issuerCertificateType: CertificateType, subjectCertificateType:CertificateType,  startDate:Date, endDate: Date, filePass: string): Observable<any> {
    const requestBody = { issuerMail, subjectMail, issuerCertificateSerialNumber,issuerCertificateType,subjectCertificateType, startDate, endDate };
    return this.http.post<any>(`http://localhost:8080/certificates/ca-certificate/${filePass}`, requestBody);
  }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>('http://localhost:8080/users/all');
  }
  getRootAndCA(): Observable<Certificate[]> {
    return this.http.get<Certificate[]>('http://localhost:8080/certificates/rootAndCA');
  }
  
}
