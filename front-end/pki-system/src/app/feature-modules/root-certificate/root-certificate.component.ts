import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../infrastructure/auth/auth.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-root-certificate',
  templateUrl: './root-certificate.component.html',
  styleUrl: './root-certificate.component.css'
})
export class RootCertificateComponent {
  startDate: Date | null = null;
  endDate: Date | null = null;
  userId!: number;

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
  }

  onCreate(): void {
    if (this.startDate && this.endDate && this.userId) {
      this.userService.createRootCertificate(this.startDate, this.endDate, this.userId)
        .subscribe(
          () => {
            alert("Root certificate created successfully!");
          },
          (error: any) => {
            alert("Something went wrong while creating your root certificate. Please try again later.");
          }
        );
    } else {
      console.error('Missing required data: startDate, endDate or userId');
      alert("Missing required data: startDate or endDate.");
    }
  }
  
}
