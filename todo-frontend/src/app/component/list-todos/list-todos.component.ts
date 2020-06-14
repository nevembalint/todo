import {Component, OnInit} from '@angular/core';
import {Todo} from "../../domain/todo";
import {TodoService} from "../../service/todo.service";
import {Router} from "@angular/router";
import {HttpParams} from "@angular/common/http";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-list-todos',
  templateUrl: 'list-todos.component.html',
  styleUrls: ['./list-todos.component.less']
})
export class ListTodosComponent implements OnInit {

  todos: Todo[];
  loading = false;
  params = new HttpParams();
  fields = ['id', 'title', 'priority', 'deadline', 'createdDate'];
  priorityValues: string[] = ['LOW', 'MEDIUM', 'HIGH'];
  selectedFields = [];
  sortAsc = true;
  deadlineValue;
  createdDateValue;
  limit: number = 10;
  offset: number = 1;
  page: number = 1;
  loadingTodoDelete: number[] = [];
  deadlineFilterType: string;
  createdDateFilterType: string;
  totalCount: number;
  numberOfPages: number;
  pageList: number[];

  constructor(
    private todoService: TodoService,
    private router: Router,
    public datePipe: DatePipe
  ) {
  }

  ngOnInit(): void {
    this.loading = true;
    this.params = this.params.set('limit', this.limit.toString());
    this.request();
  }

  get() {
    let fieldsToSend = [...this.selectedFields];
    fieldsToSend.push(this.sortAsc && fieldsToSend ? 'asc' : 'desc');
    this.params = this.params.set('sort', fieldsToSend.toString());
    this.request();
  }

  private request() {
    this.todoService.getAll(this.params).subscribe(todos => {
      this.totalCount = +todos.headers.get('X-Total-Count');
      this.numberOfPages = Math.ceil(this.totalCount / this.limit);
      this.pageList = [];
      for (let i = 0; i < this.numberOfPages; i++) {
        this.pageList.push(i + 1);
      }
      this.todos = todos.body;
      this.loading = false;
    });
  }

  filter(field: string, value: string) {
    if (value) {
      if (field === 'deadline' || field === 'createdDate') {
        if (value[0]) {
          this.params = this.params.set(field + 'From', this.datePipe.transform(value[0], 'yyyy-MM-ddTHH:mm:SS'));
        }
        if (value[1]) {
          this.params = this.params.set(field + 'Until', this.datePipe.transform(value[1], 'yyyy-MM-ddTHH:mm:SS'));
        }
      } else {
        this.params = this.params.set(field, value);
      }
    } else {
      if (field === 'deadline' || field === 'createdDate') {
        this.params = this.params.delete(field + 'From');
        this.params = this.params.delete(field + 'Until');
      } else {
        this.params = this.params.delete(field);
      }
    }
    this.resetPage();
    this.get();
  }

  pageChange() {
    this.offset = this.limit * (this.page - 1) + 1;
    this.params = this.params.set('offset', this.offset.toString());
    this.get();
  }

  previous() {
    if (this.page > 1) {
      this.page--;
      this.pageChange();
    }
  }

  next() {
    if (this.page < this.numberOfPages) {
      this.page++;
      this.pageChange();
    }
  }

  limitChange() {
    if (this.limit) {
      this.params = this.params.set('limit', this.limit.toString());
      this.resetPage();
      this.get();
    }
  }

  resetPage() {
    this.offset = 1;
    this.page = 1;
    this.params = this.params.set('offset', this.offset.toString());
  }

  editTodo(id: number) {
    this.router.navigate([`edit-todo/${id}`]);
  }

  deleteTodo(id: number) {
    this.loadingTodoDelete.push(id);
    this.todoService.deleteTodo(id).subscribe(data => {
      this.get();
      this.loadingTodoDelete = this.loadingTodoDelete.filter(todoId => todoId !== id);
    });
  }

  shouldLoadDelete(id: number) {
    return this.loadingTodoDelete.includes(id);
  }

  sortAscending() {
    this.sortAsc = true;
    this.resetPage();
    this.get();
  }

  sortDescending() {
    this.sortAsc = false;
    this.resetPage();
    this.get();
  }
}
