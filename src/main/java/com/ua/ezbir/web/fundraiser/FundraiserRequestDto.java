package com.ua.ezbir.web.fundraiser;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundraiserRequestDto {
    @NotBlank(message = "Name should not be empty!")
    private String name;

    @Min(1)
    private int amount;

    @JsonProperty("jar_link")
    private String jarLink;

    @NotBlank(message = "Description should not be empty!")
    private String description;

    @NotEmpty(message = "Categories should not be empty!")
    private Set<Category> categories;
}
