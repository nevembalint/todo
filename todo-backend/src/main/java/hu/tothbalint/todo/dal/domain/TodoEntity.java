package hu.tothbalint.todo.dal.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "todo")
@EntityListeners(AuditingEntityListener.class)
@Data
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "priority", nullable = false)
    private EntityPriority priority;

    @Column(name = "created_date", updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;
}
