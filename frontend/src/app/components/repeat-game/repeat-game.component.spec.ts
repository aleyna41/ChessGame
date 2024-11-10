import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RepeatGameComponent } from './repeat-game.component';

describe('RepeatGameComponent', () => {
  let component: RepeatGameComponent;
  let fixture: ComponentFixture<RepeatGameComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RepeatGameComponent]
    });
    fixture = TestBed.createComponent(RepeatGameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
