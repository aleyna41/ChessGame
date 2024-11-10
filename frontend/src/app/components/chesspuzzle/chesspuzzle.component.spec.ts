import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChesspuzzleComponent } from './chesspuzzle.component';

describe('ChesspuzzleComponent', () => {
  let component: ChesspuzzleComponent;
  let fixture: ComponentFixture<ChesspuzzleComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChesspuzzleComponent]
    });
    fixture = TestBed.createComponent(ChesspuzzleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
