package com.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // JPA의 auditing 기능 활성화 해주는 어노테이션
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider(){
        return new AuditorAwareImpl();
        // 아까 impl에서 뭐 상속받아서 로그인 사용자 정보를 통해 등록자, 수정자를 한다 했다.
        // 이걸 빈으로 등록한것.
    }
}
