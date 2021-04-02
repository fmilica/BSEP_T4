import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewCertificateComponent } from './new-certificate.component';

describe('NewCertificateComponent', () => {
  let component: NewCertificateComponent;
  let fixture: ComponentFixture<NewCertificateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewCertificateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewCertificateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
