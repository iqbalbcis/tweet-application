import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchSpecificUserComponent } from './search-specific-user.component';

describe('SearchSpecificUserComponent', () => {
  let component: SearchSpecificUserComponent;
  let fixture: ComponentFixture<SearchSpecificUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SearchSpecificUserComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchSpecificUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
