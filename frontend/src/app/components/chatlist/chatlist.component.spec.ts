import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChatlistComponent } from './chatlist.component';

describe('ChatlistComponent', () => {
  let component: ChatlistComponent;
  let fixture: ComponentFixture<ChatlistComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChatlistComponent]
    });
    fixture = TestBed.createComponent(ChatlistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
