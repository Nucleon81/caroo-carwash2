package com.practice.project_1.dto;

import lombok.Data;

@Data
public class ReviewRequest {
    private String review;  // Review text
    private Integer rating; // 1-5
}
