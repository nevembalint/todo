import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Todo} from "../domain/todo";
import {environment} from "../../environments/environment";
import {TodoRequest} from "../domain/todo-request";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class TodoService {

  constructor(private http: HttpClient) {
  }

  getAll(params: HttpParams) {
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');
    return this.http.get<any>(`${environment.apiUrl}/api/todo`, {params: params, observe: 'response'});
  }

  modifyTodo(id: string, todoRequest: TodoRequest): Observable<Todo> {
    return this.http.put<Todo>(`${environment.apiUrl}/api/todo/${id}`, todoRequest);
  }

  addTodo(todoRequest: TodoRequest): Observable<Todo> {
    return this.http.post<Todo>(`${environment.apiUrl}/api/todo`, todoRequest);
  }

  getTodoById(id: string): Observable<Todo> {
    return this.http.get<Todo>(`${environment.apiUrl}/api/todo/${id}`);
  }

  deleteTodo(id: number) {
    return this.http.delete<Todo>(`${environment.apiUrl}/api/todo/${id}`);
  }
}
