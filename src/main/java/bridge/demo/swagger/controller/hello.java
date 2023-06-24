package bridge.demo.swagger.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "hello", description = "hello controller")
@RestController
@RequestMapping("/api/hello")
public class hello {

	@Operation(summary = "hello 출력", description = "hello 출력메소드")
	@GetMapping("/{name}")
	public String printName(@PathVariable("name") String name) {
		return "hello";
	}

}
