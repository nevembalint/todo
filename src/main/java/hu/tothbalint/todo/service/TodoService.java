package hu.tothbalint.todo.service;

import hu.tothbalint.todo.dal.dao.TodoDao;
import hu.tothbalint.todo.dal.domain.TodoEntity;
import hu.tothbalint.todo.domain.Todo;
import hu.tothbalint.todo.web.domain.TodoRequest;
import hu.tothbalint.todo.web.transformer.TodoRequestToTodoTransformer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

    @Autowired
    private TodoRequestToTodoTransformer todoRequestToTodoTransformer;

    @Autowired
    private TodoDao todoDao;

    public Todo add(TodoRequest todoRequest) {
        return todoDao.add(todoRequestToTodoTransformer.transform(todoRequest));
    }

    public List<Todo> findAll(Specification<TodoEntity> specification, Pageable pageable) {
        return todoDao.findAll(specification, pageable);
    }

    public Todo modify(TodoRequest todoRequest, Long id) {
        return todoDao.modify(todoRequestToTodoTransformer.transform(todoRequest), id);
    }

    public void delete(Long id) {
        todoDao.delete(id);
    }
}
