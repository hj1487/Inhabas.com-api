package com.inhabas.api.domain.questionnaire.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@Table(name = "QUESTIONNAIRE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Questionnaire {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "QUESTION", nullable = false)
    private String question;

    public Questionnaire(Long id, String question) {
        this.id = id;
        this.question = question;
    }
}
