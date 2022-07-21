package com.mysite.sbb.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.Mail;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String from;
	
	public SiteUser create(String username, String email, String password) {
		SiteUser user = new SiteUser(username, email, null);
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password)); 					// 입력받은 비밀번호를 암호화하여 set.
		this.userRepository.save(user);
		return user; 
	}
	
	public SiteUser getUser(String username) {
		Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
		if (siteUser.isPresent()) {
			return siteUser.get();
		} else {
			throw new DataNotFoundException("siteuser not found");
		}
	}
	
	public SiteUser getUserById(Long id) {
		Optional<SiteUser> siteUser = this.userRepository.findById(id);
		if (siteUser.isPresent()) {
			return siteUser.get();
		} else {
			throw new DataNotFoundException("siteuser not found");
		}
	}
	
	public SiteUser findUserByEmail(String email) {
		Optional<SiteUser> siteUser = this.userRepository.findUserByEmail(email);
		if (siteUser.isPresent()) {
			return siteUser.get();
		} else {
			throw new DataNotFoundException("siteuser not found");
		}
	}
	
	public Mail createMail(String email) {
		String tempPassword = getTempPassword();
		Mail mail = new Mail();
		mail.setAddress(email);
		mail.setTitle("SBB 임시비밀번호 발급 안내 메일입니다.");
		mail.setMessage("안녕하세요. SBB 임시비밀번호 안내 관련 이메일 입니다." 
						+ " 회원님의 임시 비밀번호는 "
				        + tempPassword + " 입니다." 
						+ "로그인 후에 비밀번호를 변경을 해주세요");
		updatePassword(tempPassword, email);
		return mail;
	}
	
	public void updatePassword(String password, String email) {
		String temppassword = password;
		System.out.println("password : " + password);
		Optional<SiteUser> user = this.userRepository.findUserByEmail(email);
		this.userRepository.updatePassword(user.get().getId(), passwordEncoder.encode(temppassword));
	}
	
	public String getTempPassword() {
		char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String tempPassword = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            tempPassword += charSet[idx];
        }
		return tempPassword;
	}
	
    // 메일보내기
    public void mailSend(Mail mail) {
        System.out.println("전송 완료!");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail.getAddress());
        message.setSubject(mail.getTitle());
        message.setText(mail.getMessage());
        message.setFrom(from);
        message.setReplyTo(from);
        System.out.println("message"+message);
        javaMailSender.send(message);
    }
}
