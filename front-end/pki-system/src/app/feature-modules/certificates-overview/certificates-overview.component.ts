import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../infrastructure/auth/auth.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-certificates-overview',
  templateUrl: './certificates-overview.component.html',
  styleUrl: './certificates-overview.component.css'
})
export class CertificatesOverviewComponent {
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
}
