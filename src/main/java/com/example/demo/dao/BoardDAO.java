package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Board;

import jakarta.transaction.Transactional;

@Repository
public interface BoardDAO extends JpaRepository<Board, Integer> {
	
	//글번호와 암호를 매개변수로 전달받아 해당 레코드를 삭제
	@Modifying
	@Query("delete Board b where b.no=?1 and b.pwd=?2")
	@Transactional
	public int deleteBoard(int no, String pwd);
	
	
	@Query("select nvl(max(no),0)+1 from Board")
	public int getNextNo();
	
	
	@Modifying
	@Query(value = "insert into Board b(b.no, b.title, b.id, b.pwd, b.content, b.regdate, b.hit, b.fname, b.b_ref, b.b_level, b.b_step) values(:#{#b.no},:#{#b.title},:#{#b.member.id},:#{#b.pwd},:#{#b.content},sysdate,0,:#{#b.fname},:#{#b.b_ref},:#{#b.b_level},:#{#b.b_step})", nativeQuery = true)
	@Transactional
	public void insert(Board b);
	
	
	@Modifying
	@Query("update Board b set b.b_step = b.b_step + 1 where b.b_ref=?1 and b.b_step > ?2 ")
	@Transactional
	public void updateStep(int b_ref, int b_step);
	
	
	@Query(value = "select no,title,id,pwd,content,regdate,hit,fname,b_ref,b_level,b_step "
			+ "from (select rownum n, no,title,id,pwd,content,regdate,hit,fname,b_ref,b_level,b_step "
			+ "from (select * from board "
			+ "order by b_ref desc, b_step)) a "
			+ "where a.n between ?1 and ?2", nativeQuery = true)
	public List<Board> selectAll(int start, int end);
	
	@Query(value = "select no,title,id,pwd,content,regdate,hit,fname,b_ref,b_level,b_step "
			+ "from (select rownum n, no,title,id,pwd,content,regdate,hit,fname,b_ref,b_level,b_step "
			+ "from (select * from board where id = ?3 "
			+ "order by b_ref desc, b_step)) a "
			+ "where a.n between ?1 and ?2", nativeQuery = true)
	public List<Board> selectAll(int start, int end, String id);
	
	
	@Query(value = "select count(*) from board where id=?1", nativeQuery = true)
	public int countById(String id);
	
	
}






