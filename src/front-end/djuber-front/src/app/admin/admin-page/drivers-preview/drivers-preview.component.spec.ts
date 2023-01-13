import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriversPreviewComponent } from './drivers-preview.component';

describe('DriversPreviewComponent', () => {
  let component: DriversPreviewComponent;
  let fixture: ComponentFixture<DriversPreviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DriversPreviewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DriversPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
