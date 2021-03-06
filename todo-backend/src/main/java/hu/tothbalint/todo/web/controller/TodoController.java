package hu.tothbalint.todo.web.controller;

import hu.tothbalint.todo.dal.domain.TodoEntity;
import hu.tothbalint.todo.domain.Todo;
import hu.tothbalint.todo.service.TodoService;
import hu.tothbalint.todo.web.OffsetBasedPageRequest;
import hu.tothbalint.todo.web.domain.TodoRequest;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.EqualIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.domain.GreaterThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.LessThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200", "http://localhost:8888",
        "http://localhost:8080"})
public class TodoController {

    @Autowired
    private TodoService todoService;

    /**
     * Endpoint to add new todo.
     *
     * @param todoRequest the new todo
     * @return the created todo
     */
    @RequestMapping(value = "/api/todo", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Todo add(@RequestBody TodoRequest todoRequest) {
        return todoService.add(todoRequest);
    }

    /**
     * Endpoint to modify an existing todo.
     *
     * @param todoRequest the modified to
     * @param id          the id of the modifiable todo
     * @return the modified todo
     */
    @RequestMapping(value = "/api/todo/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Todo modify(@RequestBody TodoRequest todoRequest, @PathVariable Long id) {
        return todoService.modify(todoRequest, id);
    }

    /**
     * Endpoint to retrieve a todo by its id.
     *
     * @param id the id of the desired todo
     * @return the todo with the given id
     */
    @RequestMapping(value = "/api/todo/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Todo get(@PathVariable Long id) {
        return todoService.getById(id);
    }

    /**
     * Endpoint to delete a todo by its id.
     *
     * @param id the id of the todo that needs to be deleted
     */
    @RequestMapping(value = "/api/todo/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        todoService.delete(id);
    }

    /**
     * Endpoint to list todos.
     *
     * @param todoSpecification filtering parameters (e.g. title=cleaning)
     * @param offset            from which element should the list begin
     * @param limit             how many elements should the list contain
     * @param pageable          pageable object, responsible for sorting (e.g. sort=deadline,desc)
     * @param response          HTTP response
     * @return the list of the desired todos
     */
    @RequestMapping("/api/todo")
    @ResponseStatus(HttpStatus.OK)
    public List<Todo> list(
            @And({
                    @Spec(path = "id", spec = Equal.class),
                    @Spec(path = "title", spec = LikeIgnoreCase.class),
                    @Spec(path = "priority", spec = EqualIgnoreCase.class),
                    @Spec(path = "createdDate", params = "createdDateFrom", spec = GreaterThanOrEqual.class),
                    @Spec(path = "createdDate", params = "createdDateUntil", spec = LessThanOrEqual.class),
                    @Spec(path = "deadline", params = "deadlineFrom", spec = GreaterThanOrEqual.class),
                    @Spec(path = "deadline", params = "deadlineUntil", spec = LessThanOrEqual.class),
            }) Specification<TodoEntity> todoSpecification,
            @RequestParam(required = false, defaultValue = "1") Long offset,
            @RequestParam(required = false) Integer limit,
            Pageable pageable,
            HttpServletResponse response) {
        if (limit != null) {
            pageable = new OffsetBasedPageRequest(offset, limit, pageable.getSort());
        }
        response.addHeader("Access-Control-Expose-Headers", "X-Total-Count");
        response.addHeader("X-Total-Count", Integer.toString(todoService.findAll(todoSpecification,
                Pageable.unpaged()).size()));
        List<Todo> all = todoService.findAll(todoSpecification, pageable);
        return all;
    }
}
