import { Component, OnInit } from '@angular/core';
import { User } from '../../infrastructure/auth/model/user.model';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../user.service';
import { AuthService } from '../../infrastructure/auth/auth.service';
import { CertificateType } from '../certificates/model/certificateDto.model';
import { Certificate } from '../../infrastructure/auth/model/certificate.model';

@Component({
  selector: 'app-ca-certificate',
  templateUrl: './ca-certificate.component.html',
  styleUrl: './ca-certificate.component.css'
})
export class CaCertificateComponent implements OnInit {
  users: User[] = [];
  selectedUser: User | null = null;;
  userId: number | undefined;
  startDate: Date | null = null;
  endDate: Date | null = null;
  userMail: string | undefined;
  filePass: string | undefined;
  issuerCertificateSerialNumber: string | undefined;
  issuerCertificateType:  CertificateType | undefined;
  subjectCertificateType:  CertificateType | undefined;
  selectedCert: Certificate | null = null;;
  certificates: Certificate[]= [];


  constructor(private route: ActivatedRoute, 
    private userService: UserService, 
    private authService: AuthService, 
    private router: Router) {}

  ngOnInit(): void{
    this.authService.user$.subscribe(user => {
      if (user.id != 0) {
       this.userId = user.id;   
       this.subjectCertificateType = CertificateType.CA;
      }
    })

    this.userService.getAllUsers()
    .subscribe(
      (users: User[]) => {
        // Filter out the currently logged-in user
        this.users = users.filter(user => user.id !== this.userId);
      },
      (error: any) => {
        console.error('Error fetching users:', error);
      }
    );
    this.userService.getRootAndCA()
    .subscribe(
      (certificates: Certificate[]) => {
        this.certificates = certificates;
      },
      (error: any) => {
        console.error('Error fetching certificates:', error);
      }
    );
  }


  onCreate(): void {
    if (this.startDate && this.endDate &&  this.selectedCert!.issuerMail && this.filePass && this.selectedUser!.mail) {
      if(this.selectedCert!.certificateType == CertificateType.ROOT){
      this.userService.createCACertificate(this.selectedCert!.issuerMail, this.selectedUser!.mail, this.selectedCert!.serialNumber,  this.selectedCert!.certificateType, this.subjectCertificateType as CertificateType, this.startDate, this.endDate, this.filePass)
        .subscribe(
          () => {
            alert("CA certificate created successfully!");
          },
          (error: any) => {
            alert("Something went wrong while creating your ca certificate. Please try again later.");
          }
        );} else {
          this.userService.createCACertificate(this.selectedCert!.subjectMail, this.selectedUser!.mail, this.selectedCert!.serialNumber,  this.selectedCert!.certificateType, this.subjectCertificateType as CertificateType, this.startDate, this.endDate, this.filePass)
          .subscribe(
            () => {
              // Alert user about successful certificate creation
              alert("CA certificate created successfully!");
      
              // Fetch updated certificates after successful creation
              this.userService.getRootAndCA().subscribe(
                (certificates: Certificate[]) => {
                  this.certificates = certificates;
                  this.selectedCert = null;
                  this.selectedUser = null;
                },
                (error: any) => {
                  console.error('Error fetching certificates:', error);
                }
              );
            },
            (error: any) => {
            alert("Something went wrong while creating your ca certificate. Please try again later.");
          }
        );
        }
    } else {
      console.error('Missing required data: startDate, endDate, userId or filePass');
      alert("Missing required data: startDate, endDate, userId or filePass.");
    }
  }


  onUserSelect(user: User): void {
    this.selectedUser = user;
}

  onCertSelect(certificate: Certificate): void {
  this.selectedCert = certificate;
}


}
