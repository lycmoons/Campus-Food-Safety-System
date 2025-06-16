package com.lycmoons.util;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {
    @Value("${spring.security.jwt.key}")
    String key;  // 用于生成 token 的 key

    @Value("${spring.security.jwt.expire}")
    int expire;  // token 的有效时间（天）

    @Resource
    StringRedisTemplate stringRedisTemplate;


    // 计算过期时间
    public Date expireTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, expire*24);
        return calendar.getTime();
    }


    // 生成 token
    // 根据用户信息、用户 id 、用户 username 来生成 token
    public String createJwt(UserDetails details, int id, String username) {
        Algorithm algorithm = Algorithm.HMAC256(key);
        Date expiration = this.expireTime();
        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())  // uuid 用于唯一标识 token
                .withClaim("id", id)
                .withClaim("name", username)
                .withClaim("authorities", details.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())

                .withExpiresAt(expiration)
                .withIssuedAt(new Date())
                .sign(algorithm);
    }


    // 解析 token，返回 DecodedJWT 类型数据
    public DecodedJWT resolveJwt(String token) {
        if (token == null) {
            return null;
        }

        Algorithm algorithm = Algorithm.HMAC256(key);  // 使用相同的 key 进行解析
        JWTVerifier verifier = JWT.require(algorithm).build();  // 解析器

        try {
            DecodedJWT jwt = verifier.verify(token);

            // 检查这个 token 是否在黑名单
            if(this.isBlack(jwt.getId())) return null;

            // 检查这个 token 是否过期
            Date expireAt = jwt.getExpiresAt();
            return this.isExpire(expireAt) ? null : jwt;

        }catch (JWTVerificationException e){
            return null;
        }
    }




    /* 通过解析得到的 jwt 来获取用户信息方式如下： */

    // 获取 token 的 uuid
    public String getUuid(DecodedJWT jwt){
        return jwt.getId();
    }
    // 获取用户的 id
    public Integer getUserid(DecodedJWT jwt){
        return jwt.getClaims().get("id").asInt();
    }
    // 获取用户名
    public String getUsername(DecodedJWT jwt){
        return jwt.getClaims().get("name").asString();
    }
    // 获取用户的 UserDetails 类数据
    public UserDetails getUser(DecodedJWT jwt) {
        Map<String, Claim> claims = jwt.getClaims();
        return User
                .withUsername(this.getUsername(jwt))
                .password("******")  // token 中不会解析出用户的密码信息，这里随便填，没有意义
                .authorities(claims.get("authorities").asArray(String.class))
                .build();
    }
    // 获取 token 的过期时间
    public Date getExpiresTime(DecodedJWT jwt){
        return jwt.getExpiresAt();
    }
    //获取 token 的生成时间
    public Date getIssuedTime(DecodedJWT jwt){
        return jwt.getIssuedAt();
    }

    // 将 token 列入黑名单
    public void turnBlack(String uuid, Date expiresAt){
        Date current = new Date();
        long remainTime = expiresAt.getTime() - current.getTime();   // 剩余的有效时间，单位为毫秒

        long duration = Math.max(remainTime, 0);  // 需要存储的时间，不能为负数

        // 只需要让这个 token 在剩余的有效时间内都在黑名单中即可
        stringRedisTemplate.opsForValue().set(Const.JWT_BLACK_LIST + uuid, "", duration, TimeUnit.MILLISECONDS);
    }

    // 检查 token 是否在黑名单
    public boolean isBlack(String uuid){
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(Const.JWT_BLACK_LIST + uuid));
    }

    // 检查 token 是否过期
    public boolean isExpire(Date expiresAt){
        return new Date().after(expiresAt);
    }
}
