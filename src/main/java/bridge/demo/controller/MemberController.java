package bridge.demo.controller;

import java.time.LocalDate;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import bridge.demo.domain.Member;
import bridge.demo.dto.MemberFormDto;
import bridge.demo.dto.MemberLoginDto;
import bridge.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

	private final MemberService memberService;
	private final BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/save")
	public String saveForm(Model model) {
		model.addAttribute("MemberFormDto", new MemberFormDto());
		return "member/saveForm";
	}

	@PostMapping("/save")
	public String memberSave(@ModelAttribute("MemberFormDto") MemberFormDto form, Model model) {
		String encodedPw = passwordEncoder.encode(form.getPassword());
		Member member = new Member().builder()
			.memberId(form.getMemberId())
			.password(encodedPw)
			.email(form.getEmail())
			.created(LocalDate.now().toString())
			.build();
		try {
			memberService.save(member);
			model.addAttribute("member_id", member.getMemberId());
			return "member/success";
		} catch (IllegalStateException e) {
			log.info(e.getMessage());
			model.addAttribute("message", e.getMessage());
			return "member/fail";
		}
	}

	@GetMapping("/login")
	public String loginForm(Model model) {
		model.addAttribute("MemberLoginDto", new MemberLoginDto());
		return "member/loginForm";
	}
}
