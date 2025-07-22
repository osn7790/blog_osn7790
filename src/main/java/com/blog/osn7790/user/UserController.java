package com.blog.osn7790.user;

import com.blog.osn7790._core.errors.exception.Exception400;
import com.blog.osn7790.util.Define;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/user/update-form")
    public String updateForm( Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        User user = userService.findById(sessionUser.getId());
        model.addAttribute("user", user);

        return "user/update-form";

    }

    @PostMapping("/user/update")
    public String update( @Valid UserRequest.UpdateDTO updateDTO, Errors errors, HttpSession session) {
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);

        User updateuser = userService.updateById(sessionUser.getId(), updateDTO);
        session.setAttribute(Define.SESSION_USER, updateuser);
        return "redirect:/user/update-form";
    }

    @GetMapping("/join-form")
    public String joinForm() {
        return "user/join-form";
    }

    @PostMapping("/join")
    public String join(@Valid UserRequest.JoinDTO joinDTO, Errors errors) {

        if (errors.hasErrors()) {
            FieldError firstError = errors.getFieldErrors().get(0);
            throw new Exception400(firstError.getDefaultMessage());
        }
        userService.join(joinDTO);
        return "redirect:/login-form";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return"user/login-form";
    }

    @PostMapping("/login")
    public String login( @Valid UserRequest.LoginDTO loginDTO, Errors errors, HttpSession session) {
        User user = userService.login(loginDTO);
        if (errors.hasErrors()) {
            FieldError firstError = errors.getFieldErrors().get(0);
            throw new Exception400(firstError.getDefaultMessage());
        }

        session.setAttribute(Define.SESSION_USER, user);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }




}
