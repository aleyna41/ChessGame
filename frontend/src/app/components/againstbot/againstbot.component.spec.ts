import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgainstbotComponent } from './againstbot.component';

describe('AgainstbotComponent', () => {
  let component: AgainstbotComponent;
  let fixture: ComponentFixture<AgainstbotComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AgainstbotComponent]
    });
    fixture = TestBed.createComponent(AgainstbotComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
