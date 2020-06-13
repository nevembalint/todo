package hu.tothbalint.todo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Todo {

    private Long id;
    private String title;
    private LocalDateTime deadline;
    private Priority priority;
    private LocalDateTime createdDate;

}
