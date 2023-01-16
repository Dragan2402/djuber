import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HandleModalComponent } from './handle-modal.component';

describe('HandleModalComponent', () => {
  let component: HandleModalComponent;
  let fixture: ComponentFixture<HandleModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HandleModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HandleModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
