import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PostoRiservatoTableComponent } from './posto-riservato-table.component';

describe('PostoRiservatoTableComponent', () => {
  let component: PostoRiservatoTableComponent;
  let fixture: ComponentFixture<PostoRiservatoTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PostoRiservatoTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PostoRiservatoTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
