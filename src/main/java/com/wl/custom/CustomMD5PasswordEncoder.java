package com.wl.custom;

import cn.hutool.crypto.digest.MD5;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class CustomMD5PasswordEncoder implements PasswordEncoder {

    //加密盐
    private final String SALT = ")*3aWleQ";

    @Override
    public String encode(CharSequence rawPassword) {
        return DigestUtils.md5Hex(rawPassword.toString()+SALT);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(DigestUtils.md5Hex(rawPassword.toString()+SALT));
    }
}
