import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {TodoService} from "../../service/todo.service";
import {Todo} from "../../domain/todo";
import {TodoRequest} from "../../domain/todo-request";
import {first} from "rxjs/operators";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-add-todo',
  templateUrl: 'save-todo.component.html',
  styleUrls: ['./save-todo.component.less']
})
export class SaveTodoComponent implements OnInit {
  todoForm: FormGroup;
  loading = false;
  submitted = false;
  returnUrl: string;
  id: string;
  loadingData = true;
  priorityValues: string[] = ['LOW', 'MEDIUM', 'HIGH'];

  constructor(
    private formBuilder: FormBuilder,
    public route: ActivatedRoute,
    private router: Router,
    private todoService: TodoService,
    public datepipe: DatePipe) {

  }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    if (this.id) {
      this.todoService.getTodoById(this.id).subscribe((data: Todo) => {
        this.todoForm = this.formBuilder.group({
          title: [data.title, [Validators.required, Validators.maxLength(255)]],
          priority: [data.priority, Validators.required],
          deadline: [data.deadline]
        });
        this.loadingData = false;
      }, error => {
        this.router.navigate(['list-todos']);
      });
    } else {
      this.todoForm = this.formBuilder.group({
        title: ['', [Validators.required, Validators.maxLength(255)]],
        priority: ['', Validators.required],
        deadline: ['']
      });
      this.loadingData = false;
    }
    this.returnUrl = this.route.snapshot.queryParams.returnUrl || '/';
  }


  get f() {
    return this.todoForm.controls;
  }

  clearDeadline() {
    this.todoForm.get('deadline').reset();
  }

  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.todoForm.invalid) {
      return;
    }
    this.loading = true;
    const todoRequest: TodoRequest = new TodoRequest();
    todoRequest.title = this.f.title.value;
    todoRequest.priority = this.f.priority.value;
    todoRequest.deadline = this.datepipe.transform(this.f.deadline.value, 'yyyy-MM-ddTHH:mm:SS');

    if (this.id) {
      this.todoService.modifyTodo(this.id, todoRequest)
        .pipe(first())
        .subscribe(
          data => {
            this.router.navigate(['list-todos']);
          }, error => {
            this.loading = false;
            return;
          });
    } else {
      this.todoService.addTodo(todoRequest)
        .pipe(first())
        .subscribe(
          data => {
            this.router.navigate(['list-todos']);
          }, error => {
            this.loading = false;
            return;
          });
    }
  }
}
