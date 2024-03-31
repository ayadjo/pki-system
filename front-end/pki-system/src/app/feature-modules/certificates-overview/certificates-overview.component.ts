import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../infrastructure/auth/auth.service';
import { UserService } from '../user.service';
import { Certificate } from '../../infrastructure/auth/model/certificate.model';
import { Observable } from 'rxjs';
import { KeyStoreDto } from '../../infrastructure/auth/model/keyStoreDto.model';

@Component({
  selector: 'app-certificates-overview',
  templateUrl: './certificates-overview.component.html',
  styleUrl: './certificates-overview.component.css'
})
export class CertificatesOverviewComponent {
  certificates: Certificate[] = [];
  selectedCertificate: Certificate | undefined;
  userId!: number;
  showPasswordInput: boolean = false;
  password: string = '';
  certificate : Certificate | undefined;
  

  constructor(private userService: UserService, 
              private authService: AuthService, 
              private router: Router) {}


  ngOnInit() {    
    if (this.authService.user$) {
      this.authService.user$.subscribe(user => {
        if (user.id) {
          this.userId = user.id;
        }
      });
    }
    this.getCertificates();
  }

  getCertificates(): void {
    this.userService.getAllCertificates().subscribe({
      next: (result: Certificate[]) => {
        console.log(result);
        this.certificates = result;
      },
      error: () => {
        console.error();
      }
    });
  }

  onRevokeClicked(certificate: Certificate): void {
    
  }

  onSeeMoreClicked(certificate: Certificate): void {
    this.selectedCertificate = certificate;
    const keyStoreDto: KeyStoreDto = {
      fileName: this.selectedCertificate.serialNumber + ".jks",
      alias: this.selectedCertificate.serialNumber + this.selectedCertificate.issuerMail,
    };
    
  }
  
  
  
}
