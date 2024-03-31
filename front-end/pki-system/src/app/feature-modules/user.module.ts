import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatMenuModule } from '@angular/material/menu';
import { BrowserModule } from '@angular/platform-browser';
import { CaCertificateComponent } from './ca-certificate/ca-certificate.component';
import { RootCertificateComponent } from './root-certificate/root-certificate.component';
import { EndEntityCertificateComponent } from './end-entity-certificate/end-entity-certificate.component';
import { CertificatesOverviewComponent } from './certificates-overview/certificates-overview.component';
import { CertificateFormComponent } from './certificate-form/certificate-form.component';


@NgModule({
  declarations: [
    CaCertificateComponent,
    RootCertificateComponent,
    EndEntityCertificateComponent,
    CertificatesOverviewComponent,
    CertificateFormComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    MatFormFieldModule,
    BrowserModule,
    MatToolbarModule,
    MatMenuModule,
  ]
})
export class UserModule { }
