package bridge.demo.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

	@GetMapping("/save")
	public String saveForm(Model model) {
		model.addAttribute("memberForm", new MemberFormDto());
		return "member/saveForm";
	}

	@PostMapping("/save")
	public String memberSave(MemberFormDto form, Model model) {
		Member member = new Member().builder()
			.memberId(form.getMemberId())
			.password(form.getPassword())
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
		model.addAttribute("memberForm", new MemberFormDto());
		return "member/loginForm";
	}

	@PostMapping("/login-post")
	public String login(MemberLoginDto loginDto, Model model) {
		Member member = new Member().builder()
			.memberId(loginDto.getMemberId())
			.password(loginDto.getPassword())
			.build();
		try {
			memberService.login("아직");
			model.addAttribute("member_id", member.getMemberId());
			return "hello";
		} catch (IllegalStateException e) {
			log.info(e.getMessage());
			model.addAttribute("message", e.getMessage());
			return "member/loginForm";
		}
	}

}
