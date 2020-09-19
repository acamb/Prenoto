import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SettimanaComponent } from './settimana.component';

describe('SettimanaComponent', () => {
  let component: SettimanaComponent;
  let fixture: ComponentFixture<SettimanaComponent>;

  beforeEach(async(() => {
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
