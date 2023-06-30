package bridge.demo.hello;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/hello")
public class helloController {

	@GetMapping
	public String helloPage(Model model, HttpServletRequest request, Principal principal) {
		String memberId = principal.getName();
		log.debug("memberId={}", request.getParameter("memberId"));
		log.debug("id={}", request.getParameter("id"));
		log.debug("member_id={}", request.getParameter("member_id"));
		log.debug("userId={}", request.getParameter("userId"));
		model.addAttribute("memberId", memberId);
		return "hello";
	}

}
