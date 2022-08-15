import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { SchedaUtenteComponent } from './scheda-utente.component';

describe('SchedaUtenteComponent', () => {
  let component: SchedaUtenteComponent;
  let fixture: ComponentFixture<SchedaUtenteComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ SchedaUtenteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SchedaUtenteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
