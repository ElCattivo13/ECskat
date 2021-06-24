import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StichComponent } from './stich.component';

describe('StichComponent', () => {
  let component: StichComponent;
  let fixture: ComponentFixture<StichComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StichComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StichComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
