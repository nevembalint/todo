package hu.tothbalint.todo.dal.dao;

import hu.tothbalint.todo.dal.domain.EntityPriority;
import hu.tothbalint.todo.dal.domain.TodoEntity;
import hu.tothbalint.todo.dal.repository.TodoRepository;
import hu.tothbalint.todo.domain.Priority;
import hu.tothbalint.todo.domain.Todo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class TodoDaoTest {
    private static final long ID = 1L;
    private static final String TITLE = "title";
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDateTime FUTURE = LocalDateTime.now().plus(Duration.ofDays(1));

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @InjectMocks
    public TodoDao underTest;

    @Mock
    public TodoRepository todoRepository;

    @Test
    void testAdd() {
        // GIVEN
        TodoEntity todoEntity = TodoEntity.builder().title(TITLE).priority(EntityPriority.LOW).deadline(NOW).build();
        TodoEntity todoEntityReturn =
                TodoEntity.builder().id(ID).title(TITLE).priority(EntityPriority.LOW).deadline(NOW).build();
        Todo todo = Todo.builder().title(TITLE).priority(Priority.LOW).deadline(NOW).build();
        Todo todoReturn = Todo.builder().id(1L).title(TITLE).priority(Priority.LOW).deadline(NOW).build();
        when(todoRepository.save(todoEntity)).thenReturn(todoEntityReturn);

        // WHEN
        Todo actual = underTest.add(todo);

        // THEN
        assertEquals(todoReturn, actual);
        then(todoRepository).should(times(1)).save(todoEntity);
    }

    @Test
    void testGetShouldReturnTodoIfExists() {
        // GIVEN
        TodoEntity todoEntityReturn =
                TodoEntity.builder().id(ID).title(TITLE + TITLE).priority(EntityPriority.HIGH).deadline(NOW).build();
        Todo todoReturn = Todo.builder().id(1L).title(TITLE + TITLE).priority(Priority.HIGH).deadline(NOW).build();
        when(todoRepository.findById(ID)).thenReturn(Optional.of(todoEntityReturn));

        // WHEN
        Todo actual = underTest.getById(ID);

        // THEN
        assertEquals(todoReturn, actual);
        then(todoRepository).should(times(1)).findById(ID);
    }

    @Test
    void testGetShouldThrowExceptionIfTodoIsNotFound() {
        // GIVEN
        when(todoRepository.findById(ID)).thenReturn(Optional.empty());
        String expectedMessage = "No value present";

        // WHEN
        Exception exception = assertThrows(NoSuchElementException.class, () -> underTest.getById(ID));
        String actualMessage = exception.getMessage();

        // THEN
        assertEquals(expectedMessage, actualMessage);
        then(todoRepository).should(times(1)).findById(ID);
    }

    @Test
    void testModifyShouldModifyIfEverythingIsCorrect() {
        // GIVEN
        TodoEntity todoEntity =
                TodoEntity.builder().id(ID).title(TITLE + TITLE).priority(EntityPriority.HIGH).deadline(FUTURE).build();
        TodoEntity todoEntityFindReturn =
                TodoEntity.builder().id(ID).title(TITLE).priority(EntityPriority.LOW).deadline(NOW).build();
        TodoEntity todoEntitySaveReturn =
                TodoEntity.builder().id(ID).title(TITLE + TITLE).priority(EntityPriority.HIGH).deadline(FUTURE).build();
        Todo todo = Todo.builder().id(ID).title(TITLE + TITLE).priority(Priority.HIGH).deadline(FUTURE).build();
        Todo todoReturn = Todo.builder().id(ID).title(TITLE + TITLE).priority(Priority.HIGH).deadline(FUTURE).build();
        when(todoRepository.findById(ID)).thenReturn(Optional.of(todoEntityFindReturn));
        when(todoRepository.save(todoEntity)).thenReturn(todoEntitySaveReturn);

        // WHEN
        Todo actual = underTest.modify(todo, ID);

        // THEN
        assertEquals(todoReturn, actual);
        then(todoRepository).should(times(1)).findById(ID);
        then(todoRepository).should(times(1)).save(todoEntity);
    }

    @Test
    void testModifyShouldThrowExceptionIfTodoIsNotFound() {
        // GIVEN
        Todo todo = Todo.builder().id(ID).title(TITLE + TITLE).priority(Priority.HIGH).deadline(NOW).build();
        when(todoRepository.findById(ID)).thenReturn(Optional.empty());
        String expectedMessage = "No value present";

        // WHEN
        Exception exception = assertThrows(NoSuchElementException.class, () -> underTest.modify(todo, ID));
        String actualMessage = exception.getMessage();

        // THEN
        assertEquals(expectedMessage, actualMessage);
        then(todoRepository).should(times(1)).findById(ID);
        then(todoRepository).should(times(0)).save(any());
    }

    @Test
    void testDeleteShouldNotThrowException() {
        // GIVEN
        TodoEntity todoEntityReturn =
                TodoEntity.builder().id(ID).title(TITLE + TITLE).priority(EntityPriority.HIGH).deadline(NOW).build();
        when(todoRepository.findById(ID)).thenReturn(Optional.of(todoEntityReturn));
        // WHEN
        underTest.delete(ID);

        // THEN
        then(todoRepository).should(times(1)).findById(ID);
        then(todoRepository).should(times(1)).deleteById(ID);
    }

    @Test
    void testDeleteShouldThrowExceptionIfTodoIsNotFound() {
        // GIVEN
        when(todoRepository.findById(ID)).thenReturn(Optional.empty());
        String expectedMessage = "No value present";

        // WHEN
        Exception exception = assertThrows(NoSuchElementException.class, () -> underTest.getById(ID));
        String actualMessage = exception.getMessage();

        // THEN
        assertEquals(expectedMessage, actualMessage);
        then(todoRepository).should(times(1)).findById(ID);
        then(todoRepository).should(times(0)).deleteById(ID);
    }
}