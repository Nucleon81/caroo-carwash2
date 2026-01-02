package com.practice.project_1.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "addOns")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddOn {
    @Id
    private String id;
    private String name;
    private String description;
    private double price;
}
