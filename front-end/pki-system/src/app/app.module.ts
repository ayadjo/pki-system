import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './infrastructure/routing/app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './infrastructure/auth/login/login.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { UserModule } from './feature-modules/user/user.module';
import { AuthModule } from './infrastructure/auth/auth.module';
import { TokenInterceptor } from './infrastructure/auth/interceptor/TokenInterceptor';
import { LayoutModule } from './layout/layout.module';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatMenuModule } from '@angular/material/menu';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NavbarComponent } from './layout/navbar/navbar.component';



@NgModule({
  declarations: [
    AppComponent, 
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    UserModule,
    HttpClientModule,
    AuthModule,
    MatButtonModule,
    ReactiveFormsModule,
    LayoutModule,
    MatFormFieldModule,
    CommonModule,
    MatToolbarModule,
    MatMenuModule,
    BrowserAnimationsModule,
  ],
  providers: [ 
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true,     
    },
    
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
