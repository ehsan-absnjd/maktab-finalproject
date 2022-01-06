package ir.maktab.finalproject.aspect;

import ch.qos.logback.classic.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Aspect
@Component
public class LoggingAspect {
    Logger logger = (Logger) LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* ir.maktab.finalproject.service.*.*(..) ) || " +
            "execution(* ir.maktab.finalproject.util.*.*(..)) || " +
            "execution(* ir.maktab.finalproject.controller.*.*(..) ) )")
    public void logServicePackage(ProceedingJoinPoint jp )  {
        logger.info("Before method: " + jp.getSignature().getName());
        try {
            Object proceed = jp.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        logger.debug("After method: " + jp.getSignature().getName());
    }

}
