import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpImpComponent } from './exp-imp.component';

describe('ExpImpComponent', () => {
  let component: ExpImpComponent;
  let fixture: ComponentFixture<ExpImpComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ExpImpComponent]
    });
    fixture = TestBed.createComponent(ExpImpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
