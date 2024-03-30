import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CertificatesOverviewComponent } from './certificates-overview.component';

describe('CertificatesOverviewComponent', () => {
  let component: CertificatesOverviewComponent;
  let fixture: ComponentFixture<CertificatesOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CertificatesOverviewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CertificatesOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
