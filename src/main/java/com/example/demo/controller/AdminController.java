package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dao.MemberDAO;

import lombok.Setter;

@Controller
@Setter
public class AdminController {
	@Autowired
	private MemberDAO memberDAO;
	
	@GetMapping("/admin/member/delete/{id}")
	public ModelAndView memberDelete(@PathVariable("id") String id) {
		ModelAndView mav = new ModelAndView("redirect:/admin/index");
		memberDAO.deleteById(id);
		return mav;
	}
	
	
	
	@GetMapping("/admin/index")
	public void index(Model model) {
		model.addAttribute("list", memberDAO.findAll());
	}
}












