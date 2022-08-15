import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { IscrizioneComponent } from './iscrizione.component';

describe('IscrizioneComponent', () => {
  let component: IscrizioneComponent;
  let fixture: ComponentFixture<IscrizioneComponent>;

  beforeEach(waitForAsync(() => {
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
