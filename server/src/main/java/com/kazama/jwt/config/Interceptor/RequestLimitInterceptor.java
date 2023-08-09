package com.kazama.jwt.config.Interceptor;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.kazama.jwt.exception.RequestLimitExceededException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestLimitInterceptor implements HandlerInterceptor {
    private final int MAX_REQUEST_PER_IP = 5;
    private final String REDIS_KEY_PREFIX = "request_limit:";

    @Autowired(required = true)
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String clinetIp = getClientIP(request);
        String redisKey = REDIS_KEY_PREFIX + clinetIp;
        Integer count = (Integer) redisTemplate.opsForValue().get(redisKey);
        if (count != null && count >= MAX_REQUEST_PER_IP) {
            throw new RequestLimitExceededException("Too many attempt please try later");
        }
        redisTemplate.opsForValue().increment(redisKey, 1);
        redisTemplate.expire(redisKey, 30, TimeUnit.SECONDS);
        return true;

    }

    private String getClientIP(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

}
