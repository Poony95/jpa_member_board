package com.example.demo.service;

import java.rmi.UnexpectedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

import com.example.demo.dao.MemberDAO;
import com.example.demo.entity.Member;

import lombok.Setter;

@Service
@Setter
public class MemberService implements UserDetailsService {

	@Autowired
	private MemberDAO dao;
	
	
	// 스프링 시큐리티에서는
	// 로그인을 위한 get방식에 대한 폼태그는 우리마음대로 만들 수 있지만
	// 실제 로그인 처리는 우리가 하지 않고 
	// 시큐리티가 알아서 처리 해 줍니다.
	// 이때 다음의 loadUserByUsername메소드가 동작합니다.
	// 이 메소드는 매개변수로 사용자가 입력한 아디디가 전달되고 
	// 우리는 이 아이디에 해당하는 회원의 정보를 DAO로 가져온 다음
	// UserDatils 객체를 생성하면 반환하도록 만들어야 합니다.
	// 그 나머지 암호가 맞는지, 어떤 서비스를 위하여 
	// 권한이 있는지 등은 시큐리티가 판별합니다.
	// 만약, 해당 id의 고객이 없으면 예외를 발생시킵니다.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails user = null;
		//dao.save(new Member("kim",PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("kim") , "김유신", "user", null));
		//dao.save(new Member("lee",PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("lee") , "이순신", "admin", null));
		//dao.save(new Member("hong",PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("hong") , "홍길동", "user", null));
		//System.out.println("회원을 추가 했습니다.");
		
		Member m  = dao.findById(username).get();
		if(m == null) {
			try {
				throw new UsernameNotFoundException(username);
			}catch (Exception e) {
				// TODO: handle exception
			}
		}else {
			user = User.builder().username(username)
					.password(m.getPwd())
					.roles(m.getRole()).build();
		}
		return user;
	}

}




