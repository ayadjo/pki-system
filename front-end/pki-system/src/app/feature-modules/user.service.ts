import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../infrastructure/auth/model/user.model';
import { Observable } from 'rxjs';
import { KeyStoreDto } from '../infrastructure/auth/model/keyStoreDto.model';
import { ViewCertificateDto } from '../infrastructure/auth/model/viewCertificateDto.model';
import { CertificateType } from './certificates/model/certificateDto.model';
import { Certificate } from '../infrastructure/auth/model/certificate.model';
import { DateDto } from './certificates/model/dateDto.model';


@Injectable({
  providedIn: 'root'
})
export class UserService {
  currentUser!:any;
  private baseUrl = 'http://localhost:8080';

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

  createRootCertificate(startDate: Date, endDate: Date, issuerMail: string, filePass: string, keyUsage: string[], extendedKey: string[]): Observable<any> {
    const requestBody = { issuerMail: issuerMail, startDate: startDate, endDate: endDate, keyUsageExtension: keyUsage, extendedKey: extendedKey};
    //const requestBody = { startDate, endDate, issuerMail, keyUsage: keyUsage.map(key => ({ key })), extendedKey };
    return this.http.post<any>(`http://localhost:8080/certificates/root-certificate/${filePass}`, requestBody);
  }


  getCertificate(fileName: string, alias: string): Observable<ViewCertificateDto> {
    const url = `${this.baseUrl}/certificates/certificate/${fileName}/${alias}`;
    return this.http.get<ViewCertificateDto>(url);
  }
  
  
  createCACertificate(issuerMail: string, subjectMail: string, issuerCertificateSerialNumber: string,issuerCertificateType: CertificateType, subjectCertificateType:CertificateType,  startDate:Date, endDate: Date, filePass: string, keyUsage: string[], extendedKey: string[]): Observable<any> {
    const requestBody = { issuerMail, subjectMail, issuerCertificateSerialNumber,issuerCertificateType,subjectCertificateType, startDate, endDate, keyUsageExtension: keyUsage, extendedKey: extendedKey};
    return this.http.post<any>(`http://localhost:8080/certificates/ca-certificate/${filePass}`, requestBody);
  }

  createEECertificate(issuerMail: string, subjectMail: string, issuerCertificateSerialNumber: string,issuerCertificateType: CertificateType, subjectCertificateType:CertificateType,  startDate:Date, endDate: Date, filePass: string, keyUsage: string[], extendedKey: string[]): Observable<any> {
    const requestBody = { issuerMail, subjectMail, issuerCertificateSerialNumber,issuerCertificateType, subjectCertificateType, startDate, endDate, keyUsageExtension: keyUsage, extendedKey: extendedKey };
    return this.http.post<any>(`http://localhost:8080/certificates/ee-certificate/${filePass}`, requestBody);
  }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>('http://localhost:8080/users/all');
  }
  getRootAndCA(startDate: Date, endDate: Date): Observable<Certificate[]> {
    const url = `http://localhost:8080/certificates/rootAndCA/${startDate}/${endDate}`;
    return this.http.get<Certificate[]>(url);
  }

  getAllCertificates(): Observable<Certificate[]> {
    return this.http.get<Certificate[]>('http://localhost:8080/certificates/all');
  }

  revokeCertificate(serialNumber: string): Observable<any> {
    return this.http.put<any>(`http://localhost:8080/certificates/revoke/${serialNumber}`, {});
  }
  
}
