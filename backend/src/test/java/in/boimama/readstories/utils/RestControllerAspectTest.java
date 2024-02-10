package in.boimama.readstories.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.mockito.Mockito.when;

class RestControllerAspectTest {

    @Mock
    private Logger logger;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private CodeSignature codeSignature;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private RestControllerAspect restControllerAspect;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(joinPoint.getSignature()).thenReturn(codeSignature);

        // Ensure that joinPoint.getTarget() does not return null in your setup
        when(joinPoint.getTarget()).thenReturn(this);  // Mocking target as a non-null value

        // Simulate a web request using MockHttpServletRequest
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @Test
    void testLogBefore() throws Exception {
        // Mocking
        when(joinPoint.getTarget().getClass().getSimpleName()).thenReturn("TestClass");
        when(joinPoint.getSignature().getName()).thenReturn("testMethod");

        // Mocking parameterNames
        String[] parameterNames = {"param1", "param2"};
        when(codeSignature.getParameterNames()).thenReturn(parameterNames);

        // Mocking getArgs
        Object[] args = {"value1", "value2"};
        when(joinPoint.getArgs()).thenReturn(args);

        // when(RequestContextHolder.currentRequestAttributes()).thenReturn(new ServletRequestAttributes(request));

        // Test
        restControllerAspect.logBefore(joinPoint);

        // Verify interactions
        // verify(logger).info("Entering TestClass::testMethod"); // TODO: Not working. Fix!
    }

    @Test
    void testLogAfterReturning() {
        // Mocking
        when(joinPoint.getTarget().getClass().getSimpleName()).thenReturn("TestClass");
        when(joinPoint.getSignature().getName()).thenReturn("testMethod");
        ResponseEntity<?> responseEntity = ResponseEntity.ok().build();

        // Test
        restControllerAspect.logAfterReturning(joinPoint, responseEntity);

        // Verify interactions
        // verify(logger).info("Exiting TestClass::testMethod");
        // verify(logger).debug("Returning response: null");
    }

    @Test
    void testLogAfterThrowing() {
        // Mocking
        when(joinPoint.getTarget().getClass().getSimpleName()).thenReturn("TestClass");
        when(joinPoint.getSignature().getName()).thenReturn("testMethod");
        Throwable exception = new RuntimeException("Test exception");

        // Test
        restControllerAspect.logAfterThrowing(joinPoint, exception);

        // Verify interactions
        // verify(logger).error("Exception in TestClass::testMethod - Error message: Test exception", exception);
    }
}
