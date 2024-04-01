import { Component, OnInit } from '@angular/core';
import { User } from '../../infrastructure/auth/model/user.model';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../user.service';
import { AuthService } from '../../infrastructure/auth/auth.service';
import { CertificateType } from '../certificates/model/certificateDto.model';
import { Certificate } from '../../infrastructure/auth/model/certificate.model';
import { DateDto } from '../certificates/model/dateDto.model';


interface KeyUsage {
  DIGITAL_SIGNATURE: boolean;
  NON_REPUDIATION: boolean;
  KEY_ENCIPHER: boolean;
  DATA_ENCIPHER: boolean;
  KEY_AGREEMENT: boolean;
  CERTIFICATE_SIGNING: boolean;
  CRL_SIGNING: boolean;
  ENCIPHER_ONLY: boolean;
  DECIPHER_ONLY: boolean;
}

@Component({
  selector: 'app-ca-certificate',
  templateUrl: './ca-certificate.component.html',
  styleUrl: './ca-certificate.component.css'
})
export class CaCertificateComponent implements OnInit {
  users: User[] = [];
  selectedUser!: User ;
  userId: number | undefined;
  startDate: Date = new Date();
  endDate: Date = new Date();
  userMail: string | undefined;
  filePass: string | undefined;
  issuerCertificateSerialNumber: string | undefined;
  issuerCertificateType:  CertificateType | undefined;
  subjectCertificateType:  CertificateType | undefined;
  selectedCert!: Certificate;
  certificates: Certificate[]= [];
  certiciateButtonSelected: Boolean;
  extendedKeyUsage: any = {};
  dateDto: DateDto = { startDate: new Date(), endDate: new Date() }; 

  keyUsage: KeyUsage = {
    DIGITAL_SIGNATURE: false,
    NON_REPUDIATION: false,
    KEY_ENCIPHER: false,
    DATA_ENCIPHER: false,
    KEY_AGREEMENT: false,
    CERTIFICATE_SIGNING: false,
    CRL_SIGNING: false,
    ENCIPHER_ONLY: false,
    DECIPHER_ONLY: false,
  };


  constructor(private route: ActivatedRoute, 
    private userService: UserService, 
    private authService: AuthService, 
    private router: Router
    ) { this.certiciateButtonSelected = false;}

  ngOnInit(): void{
    this.authService.user$.subscribe(user => {
      if (user.id != 0) {
       this.userId = user.id;   
       this.subjectCertificateType = CertificateType.CA;
      }
    })
    


    //dobavljanje usera za gornju tabelu
    this.userService.getAllUsers()
    .subscribe(
      (users: User[]) => {
        // Da se ne prikazuje red od admina
        this.users = users.filter(user => user.id !== this.userId);
        
      },
      (error: any) => {
        console.error('Error fetching users:', error);
      }
    );

    //filtriranje  tabele, tako da se onemoguci da CA sertifikat potpise ROOT sertifikatu
    //this.onlyClientsWithoutRootCert();
   
  }

  startDateSelected() {
    console.log('Start date selected:', this.startDate);
  }

  endDateSelected() {
    console.log('End date selected:', this.endDate);
  }


  onlyClientsWithoutRootCert(): void {
    this.userService.getRootAndCA(this.dateDto.startDate,this.dateDto.endDate).subscribe(
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
    if (this.startDate && this.endDate &&  this.selectedCert.subjectMail && this.filePass && this.selectedUser.mail) {
      const keyUsageArray = Object.keys(this.keyUsage).filter(key => this.keyUsage[key as keyof KeyUsage]);
      const extendedKeyArray = Object.keys(this.extendedKeyUsage).filter(key => this.extendedKeyUsage[key]);
      this.userService.createCACertificate(this.selectedCert.subjectMail, this.selectedUser.mail, this.selectedCert.serialNumber,  this.selectedCert.certificateType, this.subjectCertificateType as CertificateType, this.startDate, this.endDate, this.filePass, keyUsageArray, extendedKeyArray)
        .subscribe(
          () => {
            alert("CA certificate created successfully!");
            this.userService.getRootAndCA(this.dateDto.startDate,this.dateDto.endDate).subscribe(
              (certificates: Certificate[]) => {
                this.certificates = certificates;
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
    this.dateDto.startDate = this.startDate;
    this.dateDto.endDate = this.endDate;
    
    this.userService.getRootAndCA(this.dateDto.startDate,this.dateDto.endDate)
    .subscribe(
      (certificates: Certificate[]) => {
        this.certificates = certificates;
        if(certificates.length == 0){
          alert("For chosen dates cannot find valid certificates.");
          
        }else {
          this.certiciateButtonSelected = true;
        }
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

  isInvalidDateRange(): boolean {
    if (!this.startDate || !this.endDate) {
      return false; // Ako nisu izabrani oba datuma, nema nevalidnog opsega
    }

    const start = new Date(this.startDate);
    const end = new Date(this.endDate);
    const today = new Date();

    return start > today && end > today || start > end;
  }


}
