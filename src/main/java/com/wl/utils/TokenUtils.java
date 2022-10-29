package com.wl.utils;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Payload;
import com.wl.entity.User;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class TokenUtils {

//  JWT Token结构:  JWTString=Base64(Header).Base64(Payload).HMACSHA256(base64UrlEncode(header)+"."+base64UrlEncode(payload),secret)

    public static final String SECRET = "liwang010825";

    /**
     * 生成token
     * @param userId
     * @return
     */
    public static String generatorToken(Integer userId){
        String token = JWT.create()
                .withAudience(userId.toString())   //payload  //自定义用户名
                .withExpiresAt(DateUtil.offsetHour(new Date(),2)) //指定令牌过期时间 2H
                .sign(Algorithm.HMAC256(SECRET));   //签名
        return token;
    }

    /**
     * 验证 token
     * @param token
     */
    public static void verify(String token){
        JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }

    /**
     * 获取token中 userId
     * @param token
     * @return
     */
    public static String getTokenUserId(String token){
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token).getAudience().get(0);
    }

}
