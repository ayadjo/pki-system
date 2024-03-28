import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, catchError, map, throwError } from 'rxjs';
import { User } from './model/user.model';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { ApiService } from './service/api.service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { UserService } from '../../feature-modules/user/user.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  user$ = new BehaviorSubject<User>({id: 0, mail: "", password: "", roles: [{ id: 0, name: '', permissions: [] }], commonName: "", surname : "", givenName : "", organization : "", organizationUnit : "", 
  country:"" });
  //user$ = new BehaviorSubject<User>({email: "", id: 0 });
  private access_token: string | null = null; 
  isFirstLogin: boolean = false;

  constructor(private http: HttpClient,
    private router: Router,
    private apiService: ApiService,
    private userService: UserService) { 
      const storedToken = localStorage.getItem('jwt');
      if (storedToken) {
        this.access_token = storedToken;
        this.setUser();
      }
    }

  register(user: User): Observable<any> {
    console.log('Registering user:', user);
    
    return this.http.post('http://localhost:8080/user/auth/register', user)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          console.error(error);
          return throwError("Failed to register user");
        })
      );
  }

  
  login(user: User) {
    const loginHeaders = new HttpHeaders({
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    });
    // const body = `username=${user.username}&password=${user.password}`;
    const body = {
      'username': user.mail,
      'password': user.password
    };

  
    
    
    return this.apiService.post(`http://localhost:8080/auth/login`, JSON.stringify(body), loginHeaders)
      .pipe(map((res) => {
        console.log('Login success',res);
        this.access_token = res.body.accessToken;
        localStorage.setItem("jwt", res.body.accessToken)
        this.userService.getMyInfo(user.mail);
        return res;
      }));
  }

  checkIfUserExists(): void {
    const accessToken = localStorage.getItem('access_token');
    if (accessToken == null) {
      return;
    }
    this.setUser();
  }

  private setUser(): void {
    const jwtHelperService = new JwtHelperService();
    const accessToken = this.getToken() || ""; 

    const decodedToken = jwtHelperService.decodeToken(accessToken);
    const user: User = {
      id: decodedToken.id,
      mail: decodedToken.sub,
      password: decodedToken.name || '',
      roles: decodedToken.role || '',     
      commonName: decodedToken.password || '',
      surname: decodedToken.surname || '',
      givenName: decodedToken.city || '',
      organization: decodedToken.country || '',
      organizationUnit: decodedToken.phoneNumber || '',
      country: decodedToken.confirmationPassword || ''     
    };
  
    this.user$.next(user);
  }
  

  logout() {
    this.userService.currentUser = null;
    localStorage.removeItem("jwt");
    this.access_token = null;
    this.router.navigate(['/login']);
  }

  tokenIsPresent() {
    return this.access_token != undefined && this.access_token != null;
  }

  getToken() {
    return this.access_token;
  }
}
