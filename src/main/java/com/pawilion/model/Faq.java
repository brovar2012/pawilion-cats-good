package com.pawilion.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("faqs")
@Builder
public class Faq {
    @Id
    private Long id;

    private String question;

    private String answer;

}
