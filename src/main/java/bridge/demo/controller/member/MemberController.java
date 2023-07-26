package bridge.demo.controller.member;

import java.security.Principal;
import java.time.LocalDate;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import bridge.demo.repository.member.Member;
import bridge.demo.dto.MemberFormDto;
import bridge.demo.dto.MemberLoginDto;
import bridge.demo.dto.UnregisterResponseDto;
import bridge.demo.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController implements MemberApiSpec {

	private final MemberService memberService;
	SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

	@Override
	@GetMapping("/save")
	public String saveForm(Model model) {
		model.addAttribute("MemberFormDto", new MemberFormDto());
		return "member/saveForm";
	}

	@PostMapping("/save")
	public String memberSave(@ModelAttribute("MemberFormDto") MemberFormDto form, Model model) {
		Member member = new Member().builder()
			.memberId(form.getMemberId())
			.password(form.getPassword())
			.email(form.getEmail())
			.created(LocalDate.now().toString())
			.role("USER")
			.build();
		try {
			memberService.save(member);
			model.addAttribute("member_id", member.getId());
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

	@GetMapping("/unregister")
	public String unregisterForm() {
		return "member/unregister";
	}

	@PostMapping("/unregister")
	public String unregisterMember(@RequestParam String password, Principal principal, Model model,
		HttpServletResponse response) {
		Member member = new Member().builder()
			.memberId(principal.getName())
			.password(password)
			.build();
		UnregisterResponseDto resDto = memberService.unregister(member);
		if (resDto.getStatus() == 200) {
			Cookie cookie = new Cookie("token", null);
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
			SecurityContextHolder.clearContext();
			return "redirect:/";
		} else {
			model.addAttribute("message", resDto.getMessage());
			return "member/unregisterFail";
		}
	}
}