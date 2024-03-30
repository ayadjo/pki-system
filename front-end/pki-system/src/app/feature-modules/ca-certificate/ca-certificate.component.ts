import { Component } from '@angular/core';
import { User } from '../../infrastructure/auth/model/user.model';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../user.service';
import { AuthService } from '../../infrastructure/auth/auth.service';

@Component({
  selector: 'app-ca-certificate',
  templateUrl: './ca-certificate.component.html',
  styleUrl: './ca-certificate.component.css'
})
export class CaCertificateComponent {
  users: User[] = [];
  selectedUser!: User;
  userId: number | undefined;

  constructor(private route: ActivatedRoute, 
    private userService: UserService, 
    private authService: AuthService, 
    private router: Router) {}

  ngOnInit(): void{
    this.authService.user$.subscribe(user => {
      if (user.id != 0) {
       this.userId = user.id;      
      }
    })
  }
}
