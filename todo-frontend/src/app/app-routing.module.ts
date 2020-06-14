import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ListTodosComponent} from "./component/list-todos/list-todos.component";
import {SaveTodoComponent} from "./component/save-todo/save-todo.component";


const routes: Routes = [
  {path: '', component: ListTodosComponent},
  {path: 'list-todos', component: ListTodosComponent},
  {path: 'add-todo', component: SaveTodoComponent},
  {path: 'edit-todo/:id', component: SaveTodoComponent},
  {path: '**', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
