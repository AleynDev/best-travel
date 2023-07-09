package com.aleyn.best_travel.aspects;

import com.aleyn.best_travel.util.BestTravelUtil;
import com.aleyn.best_travel.util.annotations.Notify;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
public class NotifyAspect {

    // when we make aspects the method always has to be void

    @After(value = "@annotation(com.aleyn.best_travel.util.annotations.Notify)")
    public void notifyInFile(JoinPoint joinPoint) {
        var arg = joinPoint.getArgs();
        var size = arg[1];
        var order = arg[2] == null ? "NONE" : arg[2];

        Arrays.stream(joinPoint.getArgs()).forEach(System.out::println);

        var signature = (MethodSignature) joinPoint.getSignature();
        var method = signature.getMethod();
        var annotation = method.getAnnotation(Notify.class);
        var text = String.format(LINE_FORMAT, annotation.value(), LocalDateTime.now(), size.toString(), order);

        BestTravelUtil.writeNotification(text, PATH);
    }

    // %s -> is to be replaced by a string | %f -> will be replaced by a number
    private static final String LINE_FORMAT = "At %s new %s, with size page %s and order %s";
    private static final String PATH = "files/notify.txt";

}
