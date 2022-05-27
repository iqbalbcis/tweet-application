import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MenuandheaderComponent } from './menuandheader.component';

describe('MenuandheaderComponent', () => {
  let component: MenuandheaderComponent;
  let fixture: ComponentFixture<MenuandheaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MenuandheaderComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MenuandheaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
