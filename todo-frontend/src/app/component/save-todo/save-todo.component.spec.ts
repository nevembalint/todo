import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SaveTodoComponent } from './save-todo.component';

describe('AddTodoComponent', () => {
  let component: SaveTodoComponent;
  let fixture: ComponentFixture<SaveTodoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SaveTodoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SaveTodoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
