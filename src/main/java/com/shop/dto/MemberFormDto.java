package com.shop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class MemberFormDto {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotEmpty(message = "이메일 필수")
    @Email(message = "이메일 혁식으로 써")
    private String email;

    @NotEmpty(message = "비밀번호 필수")
    @Length(min = 8, max=16, message = "비번은 8자리 이상, 16자리 이하")
    private String password;

    @NotEmpty(message = "주소는 필수")
    private String address;
}
