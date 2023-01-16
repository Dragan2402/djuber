import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriverProfileUpdatesComponent } from './driver-profile-updates.component';

describe('DriverProfileUpdatesComponent', () => {
  let component: DriverProfileUpdatesComponent;
  let fixture: ComponentFixture<DriverProfileUpdatesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DriverProfileUpdatesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DriverProfileUpdatesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
