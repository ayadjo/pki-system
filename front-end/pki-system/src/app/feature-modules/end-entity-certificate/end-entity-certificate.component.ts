import { Component, OnInit } from '@angular/core';
import { User } from '../../infrastructure/auth/model/user.model';
import { CertificateType } from '../certificates/model/certificateDto.model';
import { Certificate } from '../../infrastructure/auth/model/certificate.model';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../infrastructure/auth/auth.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-end-entity-certificate',
  templateUrl: './end-entity-certificate.component.html',
  styleUrl: './end-entity-certificate.component.css'
})
export class EndEntityCertificateComponent implements OnInit{
  users: User[] = [];
  selectedUser!: User ;
  userId: number | undefined;
  startDate: Date | null = null;
  endDate: Date | null = null;
  userMail: string | undefined;
  filePass: string | undefined;
  issuerCertificateSerialNumber: string | undefined;
  issuerCertificateType:  CertificateType | undefined;
  subjectCertificateType:  CertificateType | undefined;
  selectedCert!: Certificate;
  certificates: Certificate[]= [];
  certiciateButtonSelected: Boolean;

  constructor(private route: ActivatedRoute, 
    private userService: UserService, 
    private authService: AuthService, 
    private router: Router) {this.certiciateButtonSelected = false;}

  ngOnInit(): void{
    this.authService.user$.subscribe(user => {
      if (user.id != 0) {
        this.userId = user.id;   
        this.subjectCertificateType = CertificateType.EE;
      }
    })


    //dobavljanje usera za gornju tabelu
    this.userService.getAllUsers()
    .subscribe(
      (users: User[]) => {
        // Da se ne prikazuje mejl od admina
        this.users = users.filter(user => user.id !== this.userId);
      },
      (error: any) => {
        console.error('Error fetching users:', error);
      }
    );

    //filtriranje gornje tabele
    //this.onlyClientsWithoutRootCert();
    
  }


  onlyClientsWithoutRootCert(): void {
    this.userService.getRootAndCA().subscribe(
      (certificates: Certificate[]) => {
        // Filter out certificates with type ROOT
        const rootCertificates = certificates.filter(cert => cert.certificateType === CertificateType.ROOT);
        
        // Extract all issuerMails from filtered rootCertificates
        const issuerMails = rootCertificates.map(cert => cert.issuerMail);

        // Now get all users except the current one, who are not associated with issuers from rootCertificates
        this.userService.getAllUsers().subscribe(
          (users: User[]) => {
            this.users = users.filter(user => user.id !== this.userId && !issuerMails.includes(user.mail));
          },
          (error: any) => {
            console.error('Error fetching users:', error);
          }
        );
      },
      (error: any) => {
        console.error('Error fetching certificates:', error);
      }
    );
  }

  onCreate(): void {
    if (this.startDate && this.endDate &&  this.selectedCert.issuerMail && this.filePass && this.selectedUser.mail) {
      this.userService.createEECertificate(this.selectedCert.subjectMail, this.selectedUser.mail, this.selectedCert.serialNumber,  this.selectedCert.certificateType, this.subjectCertificateType as CertificateType, this.startDate, this.endDate, this.filePass)
        .subscribe(
          () => {
            alert("CA certificate created successfully!");
            this.userService.getAllCertificates().subscribe(
              (certificates: Certificate[]) => {
                this.certificates = certificates.filter(cert => cert.certificateType === CertificateType.CA || cert.certificateType === CertificateType.EE);
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
            (error: any) => {
            alert("Something went wrong while creating your ca certificate. Please try again later.");
          }
        
    } 
  
  chooseCertificate():void {
    //dobavljanje sertifikata za donju tabelu
        this.certiciateButtonSelected = true;
        this.userService.getAllCertificates()
        .subscribe(
          (certificates: Certificate[]) => {
          this.certificates = certificates.filter(cert => cert.certificateType === CertificateType.CA || cert.certificateType === CertificateType.EE);
          },
          (error: any) => {
            console.error('Error fetching certificates:', error);
          }
     );
    }




  onUserSelect(user: User): void {
    this.selectedUser = user;
  }

  onCertSelect(certificate: Certificate): void {
  this.selectedCert = certificate;
  }


}