package bridge.demo.hello;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@RequestMapping("/hello")
public class HelloController {

	@GetMapping
	public String helloPage(Model model, HttpServletRequest request, Principal principal) {
		String memberId = principal.getName();
		log.info("memberId={}", principal.getName());
		model.addAttribute("memberId", memberId);
		return "hello";
	}

}
