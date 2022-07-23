package com.mysite.sbb.user;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.comment.Comment;
import com.mysite.sbb.comment.CommentService;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final CommentService commentService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", 
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }
        
        try {
        userService.create(userCreateForm.getUsername(), 
                userCreateForm.getEmail(), userCreateForm.getPassword1());


        } catch (DataIntegrityViolationException e) {
        	e.printStackTrace();
        	bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
        	return "signup_form";
        } catch (Exception e) {
        	e.printStackTrace();
        	bindingResult.reject("signupFailed", e.getMessage());
        	return "signup_form";
        }
        
        return "redirect:/";
    }
    
    @GetMapping("/login")
    public String login() {
    	return "login_form";
    }
    
    @GetMapping("/login/test")
    public String loginTest(Authentication authentication, 
            @AuthenticationPrincipal SiteUser user) {
    	OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
          // System.out.println("authentication: " + userDetails.getUsername());
          System.out.println("Attributes: " + oAuth2User.getAttributes());
    	return "login_form";
    }
    
    /* user info : base */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myInfo/base/{username}")
    public String myInfoBase(@PathVariable("username") String username, Model model, Principal principal) {
    	SiteUser siteUser = this.userService.getUser(username);
		if(!siteUser.getUsername().equals(principal.getName())){							// 세션에 있는 사용자와 다른 사용자가 수정 요청했을 때
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 사용자가 아닙니다.");
		}
		
    	model.addAttribute("siteUser", siteUser);
    	
    	return "/userInfo/user_info_base";
    }
    
    /* user info : questionList */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myInfo/question/{username}")
    public String myInfoQuestion(@RequestParam(value="page", defaultValue="0") int page, @PathVariable("username") String username, Model model, Principal principal) {
    	SiteUser siteUser = this.userService.getUser(username);
		if(!siteUser.getUsername().equals(principal.getName())){							// 세션에 있는 사용자와 다른 사용자가 수정 요청했을 때
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 사용자가 아닙니다.");
		}
		
		
		Page<Question> paging = this.questionService.getListByUsername(page, siteUser);
		model.addAttribute("siteUser", siteUser);
    	model.addAttribute("paging", paging);
    	
    	return "/userInfo/user_info_question";
    }
    
    /* user info : answerList */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myInfo/answer/{username}")
    public String myInfoAnswer(@RequestParam(value="page", defaultValue="0") int page, @PathVariable("username") String username, Model model, Principal principal) {
    	SiteUser siteUser = this.userService.getUser(username);
		if(!siteUser.getUsername().equals(principal.getName())){							// 세션에 있는 사용자와 다른 사용자가 수정 요청했을 때
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 사용자가 아닙니다.");
		}
		
		Page<Answer> paging = this.answerService.getListByUsername(page, siteUser);
		model.addAttribute("siteUser", siteUser);
    	model.addAttribute("paging", paging);
    	
    	return "/userInfo/user_info_answer";
    }
    
    /* user info : commentList */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myInfo/comment/{username}")
    public String myInfoComment(@RequestParam(value="page", defaultValue="0") int page, @PathVariable("username") String username, Model model, Principal principal) {
    	SiteUser siteUser = this.userService.getUser(username);
		if(!siteUser.getUsername().equals(principal.getName())){							// 세션에 있는 사용자와 다른 사용자가 수정 요청했을 때
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 사용자가 아닙니다.");
		}
		
		Page<Comment> paging = this.commentService.getListByUsername(page, siteUser);
		model.addAttribute("siteUser", siteUser);
    	model.addAttribute("paging", paging);
    	return "/userInfo/user_info_comment";
    }
    
    /* user info : setting */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myInfo/setting")
    public String myInfoSetting(UserUpdateForm userUpdateForm) {
    	return "/userInfo/user_info_setting";
    }
    
    /* user info : setting */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/myInfo/setting")
    public String myInfoSetting(@Valid UserUpdateForm userUpdateForm, BindingResult bindingResult, Principal principal) {
    	
    	SiteUser user = this.userService.getUser(principal.getName());
    	
        if (bindingResult.hasErrors()) {
            return "/userInfo/user_info_setting";
        }
        
        // 기존 비밀번호가 맞는지 확인
        if (!passwordEncoder.matches(userUpdateForm.getCurrentPassword(), user.getPassword())) {
            bindingResult.rejectValue("currentPassword", "passwordInCorrect", "현재 비밀번호와 다릅니다.");
            return "/userInfo/user_info_setting";
        }


        if (!userUpdateForm.getPassword1().equals(userUpdateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", 
                    "2개의 패스워드가 일치하지 않습니다.");
            return "/userInfo/user_info_setting";
        }
        
        try {
        	this.userService.updatePassword(userUpdateForm.getPassword1(), user.getEmail());

        } catch (Exception e) {
        	e.printStackTrace();
        	bindingResult.reject("change Password Failed", e.getMessage());
        	return "/userInfo/user_info_setting";
        }
        
        return "redirect:/";
    }
  
}