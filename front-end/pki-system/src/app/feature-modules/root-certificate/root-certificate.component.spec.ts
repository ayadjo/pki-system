import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RootCertificateComponent } from './root-certificate.component';

describe('RootCertificateComponent', () => {
  let component: RootCertificateComponent;
  let fixture: ComponentFixture<RootCertificateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RootCertificateComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RootCertificateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
