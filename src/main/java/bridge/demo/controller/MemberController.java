package bridge.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {

	@GetMapping("/save")
	public String save() {
		return "savetest";
	}

	@PostMapping("/save")
	public String memberSave() {
		return "";
	}
}
