package hu.tothbalint.todo.dal.dao;

import hu.tothbalint.todo.dal.domain.EntityPriority;
import hu.tothbalint.todo.dal.domain.TodoEntity;
import hu.tothbalint.todo.dal.repository.TodoRepository;
import hu.tothbalint.todo.domain.Todo;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TodoDao {

    @Autowired
    private TodoRepository todoRepository;

    public Todo add(Todo todo) {
        ModelMapper modelMapper = new ModelMapper();
        TodoEntity todoEntity = todoRepository.save(modelMapper.map(todo, TodoEntity.class));
        return modelMapper.map(todoEntity, Todo.class);
    }

    public List<Todo> findAll(Specification<TodoEntity> specification, Pageable pageable) {
        ModelMapper modelMapper = new ModelMapper();
        return todoRepository.findAll(specification, pageable).stream()
                .map(todoEntity -> modelMapper.map(todoEntity, Todo.class))
                .collect(Collectors.toList());
    }

    public Todo modify(Todo todo, Long id) {
        TodoEntity existingTodoEntity = todoRepository.findById(id).orElseThrow();
        existingTodoEntity.setTitle(todo.getTitle());
        existingTodoEntity.setDeadline(todo.getDeadline());
        existingTodoEntity.setPriority(EntityPriority.valueOf(todo.getPriority().name()));
        TodoEntity savedEntity = todoRepository.save(existingTodoEntity);
        return new ModelMapper().map(savedEntity, Todo.class);
    }

    public void delete(Long id) {
        todoRepository.findById(id).orElseThrow();
        todoRepository.deleteById(id);
    }

}
