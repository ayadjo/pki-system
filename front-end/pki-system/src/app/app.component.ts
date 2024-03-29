import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { AuthService } from './infrastructure/auth/auth.service';
import { User } from './infrastructure/auth/model/user.model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'pki-system'
  isHomeRoute: boolean = false;
  user : User | undefined

  constructor(private router: Router, private authService: AuthService) {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.isHomeRoute = (event.url === '/' || event.url === '/home');
      }
    });
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
