package com.nfa.aspect;

import com.nfa.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class UserServiceErrorHandlingAspect {

    public static final String EXCEPTION_DURING_USER_AUTHENTICATION = "Exception during user authentication";
    @Autowired
    private NotificationService notificationService;

    @AfterThrowing(pointcut = "execution(* com.nfa.service.ReaderService.authenticate(..))", throwing = "ex")
    public void handleUserAuthenticationException(JoinPoint joinPoint, Exception ex) {
        logAndNotify(joinPoint, ex);
    }

    private void logAndNotify(JoinPoint joinPoint, Exception ex) {
        log.error(EXCEPTION_DURING_USER_AUTHENTICATION + ex);

        String notificationMessage = String.format(
          "[%s] %s.%s - %s",
          EXCEPTION_DURING_USER_AUTHENTICATION,
          joinPoint.getSignature().getDeclaringTypeName(),
          joinPoint.getSignature().getName(),
          ex.getMessage()
        );

        notificationService.notifyAdminAboutError(notificationMessage);
    }
}
