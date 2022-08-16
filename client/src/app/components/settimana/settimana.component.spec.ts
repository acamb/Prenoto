import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { SettimanaComponent } from './settimana.component';

describe('SettimanaComponent', () => {
  let component: SettimanaComponent;
  let fixture: ComponentFixture<SettimanaComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ SettimanaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SettimanaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
