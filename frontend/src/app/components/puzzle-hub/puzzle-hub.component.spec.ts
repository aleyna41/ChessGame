import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PuzzleHubComponent } from './puzzle-hub.component';

describe('PuzzleHubComponent', () => {
  let component: PuzzleHubComponent;
  let fixture: ComponentFixture<PuzzleHubComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PuzzleHubComponent]
    });
    fixture = TestBed.createComponent(PuzzleHubComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
