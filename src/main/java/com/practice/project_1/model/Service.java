package com.practice.project_1.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "services")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Service {
    @Id
    private String id;
    private String name;        // e.g. "Basic Wash"
    private String description;
    private Double price;       // Base price or ignored if pricing is dynamic
    // Add other fields if needed
}
