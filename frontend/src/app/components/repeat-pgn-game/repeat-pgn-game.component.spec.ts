import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RepeatPgnGameComponent } from './repeat-pgn-game.component';

describe('RepeatPgnGameComponent', () => {
  let component: RepeatPgnGameComponent;
  let fixture: ComponentFixture<RepeatPgnGameComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RepeatPgnGameComponent]
    });
    fixture = TestBed.createComponent(RepeatPgnGameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
