package com.inhabas.api.domain.club.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ClubActivityDtoTest {

  private static ValidatorFactory validatorFactory;
  private static Validator validator;

  @BeforeAll
  public static void init() {
    validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
  }

  @AfterAll
  public static void close() {
    validatorFactory.close();
  }

  @DisplayName("DTO validation 검사")
  @Test
  public void validationTest() {
    // given
    ClubActivityDto clubActivityDto =
        ClubActivityDto.builder()
            .id(-1L)
            .title("")
            .writerId(1L)
            .writerName("")
            .dateCreated(null)
            .dateUpdated(null)
            .thumbnail(null)
            .build();

    // when
    Set<ConstraintViolation<ClubActivityDto>> violations = validator.validate(clubActivityDto);

    // then
    final int allField = 5;
    assertThat(violations).hasSize(allField);
  }
}
