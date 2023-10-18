package com.kazama.SpringOAuth2.config.Interceptor;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.kazama.SpringOAuth2.util.ratelimit.BucketFactory;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestLimitInterceptor implements HandlerInterceptor {
    // private final int MAX_REQUEST_PER_IP = 5;
    // private final String REDIS_KEY_PREFIX = "request_limit:";

    // @Autowired(required = true)
    // private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private BucketFactory bucketFactory;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String uri = request.getRequestURI();

        Bucket bucket = bucketFactory.getBucket(uri);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(2);

        if (probe.isConsumed()) {
            System.out.println("Request Pass : " + request.getRequestURI());
            return true;
        }

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.getWriter().write("Rate limit exceeded. \n");

        return false;

        // int emailIndex = requestBody.indexOf("email=");
        // if (emailIndex != -1) {
        // String emailValue = requestBody.substring(emailIndex + 6);
        // int endIndex = emailValue.indexOf(")");
        // if (endIndex != -1) {
        // emailValue = emailValue.substring(0, endIndex);
        // }
        // System.out.println("Email: " + emailValue);

        // } else {
        // System.out.println("emailIndex = -1");
        // }

        // String clinetIp = getClientIP(requestWrapper);
        // System.out.println("-----------------------------------------------------");
        // String redisKey = REDIS_KEY_PREFIX + clinetIp;
        // Integer count = (Integer) redisTemplate.opsForValue().get(redisKey);
        // if (count != null && count >= MAX_REQUEST_PER_IP) {
        // throw new RequestLimitExceededException("Too many attempt please try later");
        // }
        // redisTemplate.opsForValue().increment(redisKey, 1);
        // redisTemplate.expire(redisKey, 30, TimeUnit.SECONDS);
        // System.out.println("PreHandle pass ~~");

        // return true;

    }

    private String getClientIP(ContentCachingRequestWrapper request) throws IOException {

        // String requestBody = StreamUtils.copyToString(request.getInputStream(),
        // StandardCharsets.UTF_8);

        // System.out.println("Request Body :" + requestBody);

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
        System.out.println(ipAddress);
        return ipAddress;
    }

}
