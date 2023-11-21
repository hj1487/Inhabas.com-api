package com.inhabas.api.domain.lecture.repository;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.lecture.domain.Lecture;
import com.inhabas.api.domain.lecture.domain.Student;
import com.inhabas.api.domain.lecture.dto.StudentListDto;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.testAnnotataion.DefaultDataJpaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.inhabas.api.domain.member.domain.entity.MemberTest.getTestBasicMember;
import static org.assertj.core.api.Assertions.assertThat;

@DefaultDataJpaTest
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Member instructor;

    private Lecture lecture;

//    @BeforeEach
//    public void setUp() {
//        instructor = entityManager.persist(getTestBasicMember("12171652"));
//
//        lecture = entityManager.persist(Lecture.builder()
//                .title("절권도 배우기")
//                .chief(instructor.getId())
//                .applyDeadline(LocalDateTime.of(9011, 1, 1, 1, 1, 1))
//                .curriculumDetails("1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...")
//                .daysOfWeek("월 금")
//                .introduction("호신술을 배워보자")
//                .method(1)
//                .participantsLimits(30)
//                .place("6호관 옥상")
//                .build());
//    }



    @DisplayName("수강생 정보를 불러온다.")
    @Test
    public void searchStudentsTest() {

        //given
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            entityManager.persist(getTestBasicMember(String.valueOf(100000+i), "010-0000-000" + i));
            students.add(new Student(lecture, new StudentId(String.valueOf(100000+i))));
        }
        studentRepository.saveAll(students);
        
        //when
        Integer lectureId = (Integer) ReflectionTestUtils.getField(lecture, "id");
        Page<StudentListDto> page = studentRepository.searchStudents(lectureId, PageRequest.of(0, 25));

        //then
        assertThat(page.getTotalElements()).isEqualTo(10);
        assertThat(page.getNumberOfElements()).isEqualTo(10);
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getContent().get(0)).extracting("StudentId").isEqualTo(100000);
    }


}
