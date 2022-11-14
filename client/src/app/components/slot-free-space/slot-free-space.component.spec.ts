import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SlotFreeSpaceComponent } from './slot-free-space.component';

describe('SlotFreeSpaceComponent', () => {
  let component: SlotFreeSpaceComponent;
  let fixture: ComponentFixture<SlotFreeSpaceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SlotFreeSpaceComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SlotFreeSpaceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
