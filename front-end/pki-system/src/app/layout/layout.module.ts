import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { ServicesComponent } from './services/services.component';
import { FooterComponent } from './footer/footer.component';
import { AppComponent } from '../app.component';



@NgModule({
  declarations: [
    HomeComponent,
    ServicesComponent,
    FooterComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    HomeComponent,
    ServicesComponent,
    FooterComponent
  ]
})
export class LayoutModule { }
