package bridge.demo.hello;

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

	// @GetMapping("/{name}")
	// public String printName(@PathVariable("name") String name, Model model) {
	// 	model.addAttribute("name", name);
	// 	return "hello";
	// }

	@GetMapping
	public String helloPage(Model model, HttpServletRequest request) {
		log.debug("memberId={}", request.getParameter("memberId"));
		log.debug("id={}", request.getParameter("id"));
		log.debug("member_id={}", request.getParameter("member_id"));
		log.debug("userId={}", request.getParameter("userId"));
		model.addAttribute("memberId", request.getParameter("memberId"));
		return "hello";
	}

}
