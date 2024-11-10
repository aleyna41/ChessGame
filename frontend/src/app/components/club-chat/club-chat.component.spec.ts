import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClubChatComponent } from './club-chat.component';

describe('ClubChatComponent', () => {
  let component: ClubChatComponent;
  let fixture: ComponentFixture<ClubChatComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClubChatComponent]
    });
    fixture = TestBed.createComponent(ClubChatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
