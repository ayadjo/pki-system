import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../infrastructure/auth/auth.service';
import { UserService } from '../user.service';
import { User } from '../../infrastructure/auth/model/user.model';

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
  users: User[] = [];
  selectedUser!: User;
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
  }

  onCreate(): void {
    if (this.startDate && this.endDate &&  this.selectedUser.mail && this.filePass) {
      this.userService.createRootCertificate(this.startDate, this.endDate, this.selectedUser.mail, this.filePass)
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

  onUserSelect(user: User): void {
    this.selectedUser = user;
}
  
}
