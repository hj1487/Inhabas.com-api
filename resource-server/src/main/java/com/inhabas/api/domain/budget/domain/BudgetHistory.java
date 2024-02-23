package com.inhabas.api.domain.budget.domain;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import com.inhabas.api.domain.budget.domain.valueObject.Account;
import com.inhabas.api.domain.budget.domain.valueObject.Details;
import com.inhabas.api.domain.budget.domain.valueObject.Price;
import com.inhabas.api.domain.budget.exception.BudgetHistoryNotFoundException;
import com.inhabas.api.domain.budget.exception.HistoryCannotModifiableException;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "BUDGET_HISTORY")
public class BudgetHistory extends BudgetBoard {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @AttributeOverride(name = "value", column = @Column(name = "INCOME", nullable = false))
  private Price income;

  @AttributeOverride(name = "value", column = @Column(name = "OUTCOME", nullable = false))
  private Price outcome;

  @Embedded private Account account;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_MEMBER_OF_BUDGET_IN_CHARGE"))
  private Member personInCharge;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_MEMBER_OF_BUDGET_RECEIVED"))
  private Member personReceived;

  public boolean cannotModifiableBy(Member secretary) {
    return !this.personInCharge.equals(secretary);
  }

  @Builder
  public BudgetHistory(
      Price income,
      Price outcome,
      String title,
      String details,
      Account account,
      Member writer,
      LocalDateTime dateUsed,
      Member personInCharge,
      Member personReceived) {
    super(title, details, dateUsed, writer);
    this.income = income;
    this.outcome = outcome;
    this.account = account;
    this.personInCharge = personInCharge;
    this.personReceived = personReceived;
  }

  public void modify(
      Member secretary,
      Integer income,
      Integer outcome,
      LocalDateTime dateUsed,
      String title,
      String details,
      Member personReceived) {

    if (this.id == null) {
      throw new BudgetHistoryNotFoundException(
          "cannot modify this entity, because not persisted ever!");
    }

    if (this.cannotModifiableBy(secretary)) {
      throw new HistoryCannotModifiableException();
    }

    this.title = new Title(title);
    this.details = new Details(details);
    this.dateUsed = dateUsed;
    this.income = new Price(income);
    this.outcome = new Price(outcome);
    this.personReceived = personReceived;
  }
}
