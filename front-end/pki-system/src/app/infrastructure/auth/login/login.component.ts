import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { User } from '../model/user.model';
import { UserService } from '../../../feature-modules/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{
  form!: FormGroup;
  submitted = false;
  loginError = '';

  constructor( private formBuilder: FormBuilder, private authService: AuthService, private router: Router, private userService: UserService){
  }
  ngOnInit(): void {
    this.form = this.formBuilder.group({
      mail: ['', Validators.compose([Validators.required, Validators.minLength(3), Validators.maxLength(64)])],
      password: ['', Validators.compose([Validators.required, Validators.minLength(3), Validators.maxLength(32)])]
    });
  }
  onSubmit() {
    this.submitted = true;

    this.authService.login(this.form.value)
      .subscribe({
        next: data => {
          console.log(data);
          this.router.navigate(['/']);
        },
        error: error => {
          console.log(error);
          this.submitted = false;
        }
      });


  }
}