import { Component, Input } from '@angular/core';
import { ViewCertificateDto } from '../../infrastructure/auth/model/viewCertificateDto.model';

@Component({
  selector: 'app-certificate-form',
  templateUrl: './certificate-form.component.html',
  styleUrl: './certificate-form.component.css'
})
export class CertificateFormComponent {
  @Input() certificate!: ViewCertificateDto;

}
