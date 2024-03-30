import { Component } from '@angular/core';
import { User } from '../../infrastructure/auth/model/user.model';
import { Router } from '@angular/router';
import { AuthService } from '../../infrastructure/auth/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  user : User | undefined

  constructor(private router: Router, private authService: AuthService){
    
  }

  ngOnInit(): void {
    this.authService.user$.subscribe(user => {
      this.user = user;
    });
  }

  logout():void {
    this.authService.logout();
  }
  logIn(){
    this.router.navigate(['/login']);
  }
}
  
