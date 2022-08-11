package com.ll.exam.sbb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SbbApplicationTests {
	@Autowired // questionRepository 객체를 자동으로 생성 스프링 DI기능
	private QuestionRepository questionRepository;
	@Autowired
	private AnswerRepository answerRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void testJpa() {
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		questionRepository.save(q1);

		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		questionRepository.save(q2);

		assertThat(q1.getId()).isGreaterThan(0);
		assertThat(q2.getId()).isGreaterThan(q1.getId());
	}

	@Test
	void testJpa2() {
		// SELECT * FROM question
		List<Question> all = questionRepository.findAll();
		assertEquals(2, all.size());

		Question q = all.get(0);
		assertEquals("sbb가 무엇인가요?", q.getSubject());
	}

	@Test
	void testJpa3() {
		// SELECT * FROM question
		Question q = questionRepository.findBySubject("sbb가 무엇인가요?");
		assertEquals(1, q.getId());
	}

	@Test
	void testJpa4() {
		Question q = this.questionRepository.findBySubjectAndContent(
				"sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
		assertEquals(1, q.getId());
	}

	@Test
	void testJpa5() {
		List<Question> qList = this.questionRepository.findBySubjectLike("sbb%"); // ssb로 끝나는 문자열
		Question q = qList.get(0);
		assertEquals("sbb가 무엇인가요?", q.getSubject());
	}

	@Test
	void testJpa6() {
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent()); // 값이 true인지를 테스트하는 메서드. // isPresent() null인지 아닌지 확인
		Question q = oq.get();
		q.setSubject("수정된 제목");
		this.questionRepository.save(q); // UPDATE
	}

	@Test
	void testJpa7() {
		assertEquals(2, this.questionRepository.count()); // 총 데이터수를 리턴
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		this.questionRepository.delete(q);

		assertEquals(1, this.questionRepository.count());
	}

	@Test
	void testJPA8(){
		Optional<Question> oq = this.questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		Answer a = new Answer();
		a.setContent("네 자동으로 생성됩니다.");
		a.setQuestion(q);
		a.setCreateDate(LocalDateTime.now());
		this.answerRepository.save(a);
	}

	@Test
	void testJpa9(){
		Optional<Answer> oa = this.answerRepository.findById(1);
		assertTrue(oa.isPresent());
		Answer a = oa.get();
		assertEquals(2, a.getQuestion().getId());
	}

	@Test
	void testJpa10(){
		this.questionRepository.disableForeignKeyCheck();
		this.questionRepository.truncateQuestion();
		this.questionRepository.enableForeignKeyCheck();
	}
}