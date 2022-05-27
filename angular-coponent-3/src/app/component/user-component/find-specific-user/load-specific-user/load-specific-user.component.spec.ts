import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoadSpecificUserComponent } from './load-specific-user.component';

describe('LoadSpecificUserComponent', () => {
  let component: LoadSpecificUserComponent;
  let fixture: ComponentFixture<LoadSpecificUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LoadSpecificUserComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoadSpecificUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
