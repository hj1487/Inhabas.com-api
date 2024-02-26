package com.inhabas.api.domain.contest.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.inhabas.api.domain.board.domain.BaseBoard;
import com.inhabas.api.domain.board.domain.valueObject.Content;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import com.inhabas.api.domain.contest.domain.valueObject.Association;
import com.inhabas.api.domain.contest.domain.valueObject.Topic;
import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.menu.domain.Menu;

@Entity
@Table(name = "CONTEST_BOARD")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@DiscriminatorValue("CONTEST")
public class ContestBoard extends BaseBoard {

  @ManyToOne
  @JoinColumn(name = "CONTEST_FIELD_ID", foreignKey = @ForeignKey(name = "FK_CONTEST_FIELD_OF_ID"))
  private ContestField contestField;

  @Embedded private Topic topic;

  @Embedded private Association association;

  @Embedded private Content content;

  @Column(name = "DATE_CONTEST_START", nullable = false)
  private LocalDate dateContestStart;

  @Column(name = "DATE_CONTEST_END", nullable = false)
  private LocalDate dateContestEnd;

  public String getAssociation() {
    return association.getValue();
  }

  public String getTopic() {
    return topic.getValue();
  }

  public String getContent() {
    return content.getValue();
  }

  @Builder
  public ContestBoard(
      Menu menu,
      ContestField contestField,
      String title,
      String content,
      String association,
      String topic,
      LocalDate dateContestStart,
      LocalDate dateContestEnd) {

    super(title, menu);
    this.contestField = contestField;
    this.title = new Title(title);
    this.content = new Content(content);
    this.association = new Association(association);
    this.topic = new Topic(topic);
    this.dateContestStart = dateContestStart;
    this.dateContestEnd = dateContestEnd;
  }

  // 첨부파일 수정
  public void updateFiles(List<BoardFile> files) {

    if (this.files != null) {
      this.files.clear();
    } else {
      this.files = new ArrayList<>();
    }

    for (BoardFile file : files) {
      addFile(file);
    }
  }

  // 공모전 정보 수정
  public void updateContest(
      ContestField contestField,
      String title,
      String content,
      String association,
      String topic,
      LocalDate dateContestStart,
      LocalDate dateContestEnd) {

    this.contestField = contestField;
    this.title = new Title(title);
    this.content = new Content(content);
    this.association = new Association(association);
    this.topic = new Topic(topic);
    this.dateContestStart = dateContestStart;
    this.dateContestEnd = dateContestEnd;
  }
}
