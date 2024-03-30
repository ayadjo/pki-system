import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EndEntityCertificateComponent } from './end-entity-certificate.component';

describe('EndEntityCertificateComponent', () => {
  let component: EndEntityCertificateComponent;
  let fixture: ComponentFixture<EndEntityCertificateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EndEntityCertificateComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EndEntityCertificateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
