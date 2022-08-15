import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { PostiRiservatiComponent } from './posti-riservati.component';

describe('PostiRiservatiComponent', () => {
  let component: PostiRiservatiComponent;
  let fixture: ComponentFixture<PostiRiservatiComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ PostiRiservatiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PostiRiservatiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
