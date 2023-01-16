import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserLiveChatComponent } from './user-live-chat.component';

describe('UserLiveChatComponent', () => {
  let component: UserLiveChatComponent;
  let fixture: ComponentFixture<UserLiveChatComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserLiveChatComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserLiveChatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
