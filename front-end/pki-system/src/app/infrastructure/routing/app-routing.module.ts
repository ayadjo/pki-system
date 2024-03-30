import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from '../auth/login/login.component';
import { RootCertificateComponent } from '../../feature-modules/root-certificate/root-certificate.component';
import { CaCertificateComponent } from '../../feature-modules/ca-certificate/ca-certificate.component';
import { EndEntityCertificateComponent } from '../../feature-modules/end-entity-certificate/end-entity-certificate.component';
import { CertificatesOverviewComponent } from '../../feature-modules/certificates-overview/certificates-overview.component';


const routes: Routes = [
  { path: 'login', component: LoginComponent},
  { path: 'create-root', component: RootCertificateComponent},
  { path: 'create-ca', component: CaCertificateComponent},
  { path: 'create-end-entity', component: EndEntityCertificateComponent},
  { path: 'certificates-overview', component: CertificatesOverviewComponent},

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
