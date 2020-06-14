package hu.tothbalint.todo.web.transformer;

import hu.tothbalint.todo.domain.Priority;
import hu.tothbalint.todo.domain.Todo;
import hu.tothbalint.todo.web.domain.TodoRequest;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TodoRequestToTodoTransformerTest {
    private static final String TITLE = "title";
    private static final LocalDateTime NOW = LocalDateTime.now();

    TodoRequestToTodoTransformer underTest = new TodoRequestToTodoTransformer();

    @Test
    public void testTransformShouldTransformIfEverythingIsCorrect() {
        // GIVEN
        TodoRequest todoRequest =
                TodoRequest.builder().title(TITLE).priority("HIGH").deadline(NOW.toString()).build();
        Todo todo = Todo.builder().title(TITLE).priority(Priority.HIGH).deadline(NOW).build();

        // WHEN
        Todo actual = underTest.transform(todoRequest);

        // THEN
        assertEquals(todo, actual);
    }

    @Test
    public void testTransformShouldTransformIfDeadlineIsMissing() {
        // GIVEN
        TodoRequest todoRequest =
                TodoRequest.builder().title(TITLE).priority("HIGH").build();
        Todo todo = Todo.builder().title(TITLE).priority(Priority.HIGH).build();

        // WHEN
        Todo actual = underTest.transform(todoRequest);

        // THEN
        assertEquals(todo, actual);
    }
}