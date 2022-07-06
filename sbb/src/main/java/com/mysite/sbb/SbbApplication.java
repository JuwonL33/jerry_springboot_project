package com.mysite.sbb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * @SpringBootApplication 내부에서 ComponentScan이 기본 패키지에 대해서 설정되어 있기 때문에 직접 설정하지 않아도 자동으로 ComponentScan이 되어
 * 등록된 Bean을 사용할 수 있는 것임. 
 */

@SpringBootApplication
public class SbbApplication {
	public static void main(String[] args) {
		SpringApplication.run(SbbApplication.class, args);
	}

}
