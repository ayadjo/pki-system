import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CaCertificateComponent } from './ca-certificate.component';

describe('CaCertificateComponent', () => {
  let component: CaCertificateComponent;
  let fixture: ComponentFixture<CaCertificateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CaCertificateComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CaCertificateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
