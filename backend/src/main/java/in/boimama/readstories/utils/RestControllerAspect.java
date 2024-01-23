package in.boimama.readstories.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class RestControllerAspect {
    private static final Logger logger = LoggerFactory.getLogger(RestControllerAspect.class);

    @Autowired
    private ObjectMapper mapper;

    @Pointcut("within(in.boimama.readstories.controller.AdminController)")
    public void adminControllerPointcut() {
    }

    @Pointcut("within(in.boimama.readstories.controller.AuthorController)")
    public void authorControllerPointcut() {
    }

    @Pointcut("within(in.boimama.readstories.controller.StoryController)")
    public void storyControllerPointcut() {
    }

    @Pointcut("within(in.boimama.readstories.controller.UserController)")
    public void userControllerPointcut() {
    }

    @Pointcut("adminControllerPointcut() || authorControllerPointcut() || storyControllerPointcut() || userControllerPointcut()")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void logBefore(JoinPoint joinPoint) throws JsonProcessingException {
        final String className = joinPoint.getTarget().getClass().getSimpleName();
        final String methodName = joinPoint.getSignature().getName();
        logger.info("Entering {}::{}", className, methodName);

        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        logger.info("Received request for {} with parameter: {}",
                request.getRequestURI(), mapper.writeValueAsString(getParameters(joinPoint)));
    }

    @AfterReturning(pointcut = "pointcut()", returning = "responseEntity")
    public void logAfterReturning(JoinPoint joinPoint, ResponseEntity<?> responseEntity) {
        final String className = joinPoint.getTarget().getClass().getSimpleName();
        final String methodName = joinPoint.getSignature().getName();
        logger.info("Exiting {}::{}", className, methodName);
        logger.debug("Returning response: {}", responseEntity.getBody());
    }

    @AfterThrowing(pointcut = "pointcut()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        logger.error("Exception in {}::{} - Error message: {}", className, methodName, ex.getMessage(), ex);
    }

    private Map<String, Object> getParameters(JoinPoint joinPoint) {
        final CodeSignature signature = (CodeSignature) joinPoint.getSignature();
        final HashMap<String, Object> parameters = new HashMap<>();
        final String[] parameterNames = signature.getParameterNames();
        for (int i = 0; i < parameterNames.length; i++) {
            if (HttpServletRequest.class.isAssignableFrom(joinPoint.getArgs()[i].getClass()))
                continue; // Ignore if it's HttpServletRequest parameter
            parameters.put(parameterNames[i], joinPoint.getArgs()[i]);
        }
        return parameters;
    }
}
