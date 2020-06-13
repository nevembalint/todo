package hu.tothbalint.todo.web.transformer;

import hu.tothbalint.todo.domain.Todo;
import hu.tothbalint.todo.web.domain.TodoRequest;

import java.time.LocalDateTime;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TodoRequestToTodoTransformer {

    public Todo transform(TodoRequest todoRequest) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        Todo todo = modelMapper.map(todoRequest, Todo.class);
        todo.setDeadline(LocalDateTime.parse(todoRequest.getDeadline()));
        return todo;
    }

}
