import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IscrizioneComponent } from './iscrizione.component';

describe('IscrizioneComponent', () => {
  let component: IscrizioneComponent;
  let fixture: ComponentFixture<IscrizioneComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IscrizioneComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IscrizioneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
