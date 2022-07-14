package com.mysite.sbb;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/*
 * @author juwonlee
 * @RequiredArgsConstructor
 * hello, lombok 속성에 final을 적용하고 롬복의 @RequiredArgsConstructor 애너테이션을 적용하면 해당 속성을 필요로하는 생성자가 롬복에 의해 자동으로 생성된다. (※ final이 없는 속성은 생성자에 포함되지 않는다.)
 * final을 적용했기 때문에 @Setter는 의미가 없으며 Setter 메서드들도 사용할수 없다. final은 한번 설정한 값을 변경할수 없게 하는 키워드이다.
 * 해당 애너테이션은 의존성 주입(Dependency Injection)시 사용됨.
 */

@RequiredArgsConstructor
@Getter
public class HelloLombok {

	private final String hello;
	private final int lombok;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloLombok helloLombok = new HelloLombok("헬로", 5);

		System.out.println(helloLombok.getHello());
		System.out.println(helloLombok.getLombok());
	}
}
