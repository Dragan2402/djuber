import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideStartNotificationDialogComponent } from './ride-start-notification-dialog.component';

describe('RideStartNotificationDialogComponent', () => {
  let component: RideStartNotificationDialogComponent;
  let fixture: ComponentFixture<RideStartNotificationDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RideStartNotificationDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideStartNotificationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
