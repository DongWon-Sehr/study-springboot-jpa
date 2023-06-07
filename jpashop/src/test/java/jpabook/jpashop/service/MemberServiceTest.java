package jpabook.jpashop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.memberRepository;

@ExtendWith(SpringExtension.class) // ExtendWith annotation is Junit 5 version of RunWith(SpringRunner.class)
@SpringBootTest // test will be failed to be autowired w/o this annotation
@Transactional // rollback after test
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired memberRepository memberRepository;
    @Autowired EntityManager em;
    
    @Test
    public void join() throws Exception {
        
        // given
        Member member = new Member();
        member.setName("kim");

        // when
        Long savedId = memberService.join(member);

        // then
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    public void duplicateMemberException() throws Exception {
        
        // given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        // when
        memberService.join(member1);
        assertThrows(IllegalStateException.class, () ->  memberService.join(member2)); // throw exception

        // then
    }
}
