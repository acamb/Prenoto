import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SchedaUtenteComponent } from './scheda-utente.component';

describe('SchedaUtenteComponent', () => {
  let component: SchedaUtenteComponent;
  let fixture: ComponentFixture<SchedaUtenteComponent>;

  beforeEach(async(() => {
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
