package hu.tothbalint.todo.dal.repository;

import hu.tothbalint.todo.dal.domain.TodoEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Component;

@Component
public class TodoRepository {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Method to list todos.
     *
     * @param pageable pageable object, responsible for sorting (e.g. sort=deadline,desc)
     * @return the list of the desired todos
     */
    public List<TodoEntity> findAll(Specification<TodoEntity> specification, Pageable pageable) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        List<TodoEntity> todoEntities = new ArrayList<>();
        try {
            tx = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<TodoEntity> criteriaQuery = criteriaBuilder.createQuery(TodoEntity.class);
            Root<TodoEntity> root = criteriaQuery.from(TodoEntity.class);
            criteriaQuery.select(root);

            if (specification != null) {
                criteriaQuery.where(specification.toPredicate(root, criteriaQuery, criteriaBuilder));
            }

            Query<TodoEntity> query;
            if (!pageable.equals(Pageable.unpaged())) {
                criteriaQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), root, criteriaBuilder));
                query = session.createQuery(criteriaQuery);
                query.setFirstResult((int) pageable.getOffset());
                query.setMaxResults(pageable.getPageSize());
            } else {
                query = session.createQuery(criteriaQuery);
            }

            todoEntities = query.getResultList();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return todoEntities;
    }

    /**
     * Method to save a todo.
     *
     * @param todoEntity entity to be saved
     * @return saved entity
     */
    public TodoEntity save(TodoEntity todoEntity) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(todoEntity);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return findById(todoEntity.getId()).get();
    }

    /**
     * Method to retrieve a todo by its id.
     *
     * @param id of the desired todo
     * @return the todo with the given id
     */
    public Optional<TodoEntity> findById(Long id) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Optional<TodoEntity> optionalTodoEntity = Optional.empty();
        try {
            tx = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<TodoEntity> criteriaQuery = criteriaBuilder.createQuery(TodoEntity.class);
            Root<TodoEntity> root = criteriaQuery.from(TodoEntity.class);
            criteriaQuery.select(root);

            criteriaQuery.where(criteriaBuilder.equal(root.get("id"), id));

            Query<TodoEntity> query = session.createQuery(criteriaQuery);
            try {
                optionalTodoEntity = Optional.of(query.getSingleResult());
            } catch (NoResultException e) {
                e.printStackTrace();
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return optionalTodoEntity;
    }

    /**
     * Method to delete a todo by its id.
     *
     * @param id the id of the todo that needs to be deleted
     */
    public void deleteById(Long id) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaDelete<TodoEntity> criteriaQuery = criteriaBuilder.createCriteriaDelete(TodoEntity.class);
            Root<TodoEntity> root = criteriaQuery.from(TodoEntity.class);
            criteriaQuery.where(criteriaBuilder.equal(root.get("id"), id));

            Query<TodoEntity> query = session.createQuery(criteriaQuery);
            query.executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

    }
}
