import { Component } from '@angular/core';
import { User } from '../../infrastructure/auth/model/user.model';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../user.service';
import { AuthService } from '../../infrastructure/auth/auth.service';
import { CertificateType } from '../certificates/model/certificateDto.model';

@Component({
  selector: 'app-ca-certificate',
  templateUrl: './ca-certificate.component.html',
  styleUrl: './ca-certificate.component.css'
})
export class CaCertificateComponent {
  users: User[] = [];
  selectedUser!: User;
  userId: number | undefined;
  startDate: Date | null = null;
  endDate: Date | null = null;
  userMail: string | undefined;
  filePass: string | undefined;
  issuerCertificateSerialNumber: string | undefined;
  issuerCertificateType:  CertificateType | undefined;
  subjectCertificateType:  CertificateType | undefined;

  constructor(private route: ActivatedRoute, 
    private userService: UserService, 
    private authService: AuthService, 
    private router: Router) {}

  ngOnInit(): void{
    this.authService.user$.subscribe(user => {
      if (user.id != 0) {
       this.userId = user.id;   
       this.userMail = user.mail;
       this.getIssuerCertificateSerialNumber();  
       this.getIssuerCertificateType();  
       this.subjectCertificateType = CertificateType.CA;
      }
    })

    this.userService.getAllUsers()
    .subscribe(
      (users: User[]) => {
        this.users = users;
      },
      (error: any) => {
        console.error('Error fetching users:', error);
      }
    );
  }


  onCreate(): void {
    if (this.startDate && this.endDate &&  this.userMail && this.filePass) {
      this.userService.createCACertificate(this.userMail, this.selectedUser.mail, this.issuerCertificateSerialNumber as string,  this.issuerCertificateType as CertificateType, this.subjectCertificateType as CertificateType, this.startDate, this.endDate, this.filePass)
        .subscribe(
          () => {
            alert("CA certificate created successfully!");
          },
          (error: any) => {
            alert("Something went wrong while creating your ca certificate. Please try again later.");
          }
        );
    } else {
      console.error('Missing required data: startDate, endDate, userId or filePass');
      alert("Missing required data: startDate, endDate, userId or filePass.");
    }
  }

  getIssuerCertificateSerialNumber(): void {
    if (this.userMail) {
      this.userService.getIssuerCertificateSerialNumber(this.userMail)
        .subscribe(
          (serialNumber: string) => {
            this.issuerCertificateSerialNumber = serialNumber;
          },
          (error: any) => {
            console.error('Error fetching issuer certificate serial number:', error);
          }
        );
    } else {
      console.error('Missing required data: userMail');
    }
  }

  getIssuerCertificateType(): void {
    if (this.userMail) {
      this.userService.getIssuerCertificateType(this.userMail)
        .subscribe(
          (type: CertificateType) => {
            this.issuerCertificateType = type;
          },
          (error: any) => {
            console.error('Error fetching issuer certificate type:', error);
          }
        );
    } else {
      console.error('Missing required data: userMail');
    }
  }

  onUserSelect(user: User): void {
    this.selectedUser = user;
}


}
