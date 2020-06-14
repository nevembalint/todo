package hu.tothbalint.todo.web.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoRequest {
    @NotNull
    @Size(max = 255)
    private String title;
    private String deadline;
    @NotNull
    private String priority;

}
