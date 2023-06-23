package bridge.demo.hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hello")
public class helloController {

	@GetMapping("/{name}")
	public String printName(@PathVariable("name") String name, Model model) {

		model.addAttribute("name", name);
		return "hello";
	}

}
