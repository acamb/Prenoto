import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { IscrizioniComponent } from './iscrizioni.component';

describe('IscrizioniComponent', () => {
  let component: IscrizioniComponent;
  let fixture: ComponentFixture<IscrizioniComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ IscrizioniComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IscrizioniComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
