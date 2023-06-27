package bridge.demo.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import bridge.demo.domain.Member;
import bridge.demo.dto.MemberForm;
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
		model.addAttribute("memberForm", new MemberForm());
		return "member/saveForm";
	}

	@PostMapping("/save")
	public String memberSave(MemberForm form, Model model) {
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
			return "member/fail";
		}
	}
}
