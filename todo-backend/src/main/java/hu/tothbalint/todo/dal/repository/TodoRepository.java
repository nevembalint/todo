package hu.tothbalint.todo.dal.repository;

import hu.tothbalint.todo.dal.domain.TodoEntity;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends CrudRepository<TodoEntity, Long> {

    List<TodoEntity> findAll(Specification<TodoEntity> specification, Pageable pageable);

}
