import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../infrastructure/auth/auth.service';
import { UserService } from '../user.service';

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
  selector: 'app-root-certificate',
  templateUrl: './root-certificate.component.html',
  styleUrl: './root-certificate.component.css'
})

export class RootCertificateComponent {
  startDate: Date | null = null;
  endDate: Date | null = null;
  userId!: number;
  userMail: string | undefined;
  filePass: string | undefined;
  extendedKeyUsage: any = {};

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

  constructor(private userService: UserService, 
              private authService: AuthService, 
              private router: Router) {}


  ngOnInit() {    
    if (this.authService.user$) {
      this.authService.user$.subscribe(user => {
        if (user.id) {
          this.userId = user.id;
          this.userMail = user.mail;
        }
      });
    }
  }

  onCreate(): void {
    if (this.startDate && this.endDate &&  this.userMail && this.filePass) {
      const keyUsageArray = Object.keys(this.keyUsage).filter(key => this.keyUsage[key as keyof KeyUsage]);
      const extendedKeyArray = Object.keys(this.extendedKeyUsage).filter(key => this.extendedKeyUsage[key]);
      this.userService.createRootCertificate(this.startDate, this.endDate, this.userMail, this.filePass, keyUsageArray, extendedKeyArray)
        .subscribe(
          () => {
            alert("Root certificate created successfully!");
          },
          (error: any) => {
            alert("Something went wrong while creating your root certificate. Please try again later.");
          }
        );
    } else {
      console.error('Missing required data: startDate, endDate, userId or filePass');
      alert("Missing required data: startDate, endDate, userId or filePass.");
    }
  }

  private getSelectedOptions(options: any): string[] {
    return Object.entries(options)
        .filter(([key, value]) => value)
        .map(([key, value]) => key);
  }

  
  
}
