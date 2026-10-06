package com.minimart.dto;

import lombok.*;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryUpdateDTO {

    @NotNull(message = "Category ID is required")
    private Long id;

    private String name;

    private String description;
}
