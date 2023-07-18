package bridge.demo.swagger.controller;

import java.security.Principal;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import bridge.demo.dto.MemberFormDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "Member", description = "Member Controller")
public interface MemberApiSpec {

	@Operation(summary = "회원가입 페이지 이동", description = "회원가입 페이지 이동 메소드")
	String saveForm(Model model);

	@Operation(summary = "회원가입", description = "회원 등록 메소드")
	String memberSave(@ModelAttribute("MemberFormDto") MemberFormDto form, Model model);

	@Operation(summary = "로그인 페이지 이동", description = "로그인 페이지 이동 메소드")
	String loginForm(Model model);

	@Operation(summary = "회원탈퇴 페이지 이동", description = "회원탈퇴 페이지 이동 메소드")
	String unregisterForm();

	@Operation(summary = "회원탈퇴", description = "회원탈퇴 메소드")
	String unregisterForm(@RequestParam String password, Principal principal, Model model,
		HttpServletResponse response);
}
