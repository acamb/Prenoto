import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { RiepilogoIscrizioniComponent } from './riepilogo-iscrizioni.component';

describe('RiepilogoIscrizioniComponent', () => {
  let component: RiepilogoIscrizioniComponent;
  let fixture: ComponentFixture<RiepilogoIscrizioniComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ RiepilogoIscrizioniComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RiepilogoIscrizioniComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
