package com.itzroma.astrocornerapi.aop;

import com.itzroma.astrocornerapi.exception.RateLimitException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Configuration
public class RateLimiterAspect {

    private final Map<String, Bucket> bucketCache = new ConcurrentHashMap<>();

    @Around("@annotation(rateLimiter)")
    public Object rateLimit(ProceedingJoinPoint joinPoint, RateLimiter rateLimiter) throws Throwable {
        // Get the HTTP request object
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // Get the IP address of the client
        String clientIpAddress = request.getRemoteAddr();

        // Get the bucket for the IP address (create one if it doesn't exist)
        Bucket bucket = bucketCache.computeIfAbsent(clientIpAddress, k -> createNewBucket());

        // Try to consume a token from the bucket
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            // If a token was consumed, proceed with the method invocation
            return joinPoint.proceed();
        } else {
            // If a token was not consumed, return a rate limiting error response
            long waitForRefillSec = probe.getNanosToWaitForRefill() / 1_000_000_000;
            throw new RateLimitException("Too many requests. Please wait for " + waitForRefillSec + "s before trying again.");
        }
    }

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.classic(3, Refill.intervally(3, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }
}