package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.BoardDAO;
import com.example.demo.entity.Board;

import lombok.Setter;

@Service
@Setter
public class BoardService {
	@Autowired
	private BoardDAO dao;
	
	//전체레코드 수를 반환하는 메소드정의
	public int getTotalRecord() {
		return (int)dao.count();
	}
	
	public List<Board> findAll(int start, int end){
		//return dao.findAll();
		return dao.selectAll(start, end);
	}
	
	public Board findById(int no) {
		return dao.findById(no).get();
	}
	
	public int getNextNo() {
		return dao.getNextNo();
	}
	
	public void insert(Board b) {
		dao.insert(b);
	}
	
	public void updateStep(int b_ref, int b_step) {
		dao.updateStep(b_ref, b_step);
	}
	
	public int deleteBoard(int no, String pwd) {
		return dao.deleteBoard(no, pwd);
	}

	public int getTotalRecord(String id) {
		// TODO Auto-generated method stub
		return dao.countById(id);
	}

	public Object findAll(int start, int end, String id) {
		// TODO Auto-generated method stub
		return dao.selectAll(start, end, id);
	}
}



















