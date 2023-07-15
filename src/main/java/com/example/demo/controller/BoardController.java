package com.example.demo.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.net.http.HttpRequest;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dao.MemberDAO;
import com.example.demo.entity.Board;
import com.example.demo.service.BoardService;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;

@Controller
@Setter
public class BoardController {
	public int pageSIZE = 10;
	public int totalRecord;
	public int totalPage;
	
	@Autowired
	private BoardService bs;
	
	@Autowired
	private MemberDAO memberDAO;
	
	
	@GetMapping("/board/list/{pageNUM}/{id}")
	public String list(Model model, 
			@PathVariable("pageNUM")  int pageNUM,
			@PathVariable("id") String id,
			HttpSession session) {	
		
		System.out.println("id:"+id);
		
		//로그인한 회원의 상태유지
		
		//로그인한 회원의 정보를 갖고 오기 위하여
		//Authentication 객체를 생성합니다.
		Authentication authentication
		= SecurityContextHolder.getContext().getAuthentication();
		
		//Authentication 객체를 통해서 로그인한 회원의 
		//정보를 갖고 옵니다
		User user=(User) authentication.getPrincipal();
		
		//user를 통해서 로그인한 회원의 아이디를 갖고 옵니다.
		String loginId = user.getUsername();
		
		//아이디만 상태유지 할 수도 있고
		//dao를 통해서 회원객체를 갖고와 그것을 상태유지 할 수도 있어요
		session.setAttribute("user", memberDAO.findById(loginId).get());
		//session.setAttribute("id", loginId);
		
		if(id.equals("all")) {
			totalRecord = bs.getTotalRecord();
		}else {
			totalRecord = bs.getTotalRecord(id);
		}
		
		totalPage = (int)Math.ceil(totalRecord/(double)pageSIZE);
		System.out.println("전체레코드수:"+totalRecord);
		System.out.println("전체페이지수:"+totalPage);
		System.out.println("현재페이지:"+pageNUM);
		int start = (pageNUM-1)*pageSIZE+1;
		int end = start+pageSIZE-1;
		System.out.println("start:"+start);
		System.out.println("end:"+end);
		
		if(id.equals("all")) {
			model.addAttribute("list", bs.findAll(start, end));
		}else {
			model.addAttribute("list", bs.findAll(start, end, id));
		}
		
		
		model.addAttribute("totalPage", totalPage);
		session.setAttribute("id", id);
		return "/board/list";
	}
	
	@GetMapping("/board/detail/{no}")
	public ModelAndView detail(@PathVariable("no") int no) {
		System.out.println("글번호:"+no);
		ModelAndView mav = new ModelAndView("/board/detail");
		mav.addObject("b", bs.findById(no));
		return mav;
		
	}
	
	@GetMapping("/board/insert/{no}")
	public String insert(Model model, 
			@PathVariable("no") int no) {
		System.out.println("insert 컨트롤러 동작함.");
		model.addAttribute("no", no);
		return "/board/insert";
	}
	
	
	@PostMapping("/board/insert")
	public ModelAndView insert(Board b, HttpServletRequest request) {		
		String path = request.getServletContext().getRealPath("/images");		
		System.out.println("path:"+path);
		String fname = null;
		MultipartFile uploadFile = b.getUploadFile();
		fname = uploadFile.getOriginalFilename();
		if(fname != null && !fname.equals("")) {
			try {
				FileOutputStream fos = new FileOutputStream(path+"/"+fname);				
				FileCopyUtils.copy(uploadFile.getBytes(), fos);
				fos.close();
			}catch (Exception e) {
				System.out.println("예외발생:"+e.getMessage());
			}
		}else {
			fname = "";
		}
		
		b.setFname(fname);
		
		
		ModelAndView mav = new ModelAndView("redirect:/board/list/1/all");
		
		//일단 새글이라고 봅니다.
		int no = bs.getNextNo();
		int b_ref = no;
		int b_level = 0;
		int b_step = 0;
		
		//답글에 대한 처리
		int pno = b.getNo();
		if(pno != 0) {
			Board p = bs.findById(pno);
			b_ref = p.getB_ref();
			b_level = p.getB_level();
			b_step = p.getB_step();
			bs.updateStep(b_ref, b_step);
			b_level++;
			b_step++;
		}
				
		b.setNo(no);
		b.setB_ref(b_ref);
		b.setB_level(b_level);
		b.setB_step(b_step);
		
		bs.insert(b);
		return mav;
	}
	
	@GetMapping("/board/delete/{no}")
	public String delete(@PathVariable("no") int no, Model model) {
		model.addAttribute("no", no);
		return "/board/delete";
	}
	
	@PostMapping("/board/delete")
	public ModelAndView delete(int no, String pwd, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("redirect:/board/list/1");
		String path = request.getServletContext().getRealPath("/images");
		String fname = bs.findById(no).getFname();
		if(bs.deleteBoard(no, pwd) == 1) {
			if(fname != null && !fname.equals("")) {
				File file = new File(path+"/"+fname);
				file.delete();
			}
		}
		return mav;
	}
}











