package com.gpt.wechat.common.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhaoXu
 * @date 2023/4/15 16:13
 */
@Component
@Aspect
@Slf4j
public class LogMethodAspect {

    @Autowired
    private ObjectMapper objectMapper;

    @Pointcut(" @annotation(com.gpt.wechat.common.aspect.LogAround)")
    private void logAround() {

    }

    @Around("logAround()")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();

        // 类名
        String className = signature.getDeclaringTypeName();
        int lastPointIndex = className.lastIndexOf(".");
        className = className.substring(lastPointIndex + 1);

        // 方法名
        String methodName = signature.getName();
        // 方法参数名字和值
        Map<String, Object> paramNameAndValue = getParamNameAndValue(joinPoint);
        methodName = className + "." + methodName + " ";
        log.info(methodName + "execute before, requestBody: {}", objectMapper.valueToTree(paramNameAndValue));

        Object proceed = null;
        long startTime = System.currentTimeMillis();
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable e) {
            log.error(methodName + "execute error, exception: {}", objectMapper.valueToTree(e));
            throw new RuntimeException(e);
        } finally {
            log.info(methodName + "execute after, responseBody: {}", objectMapper.valueToTree(proceed));
            log.info(methodName + "execute time: {}", System.currentTimeMillis() - startTime + " millisecond");
        }
        return proceed;
    }

    /**
     * 获取参数Map集合
     *
     * @param joinPoint
     * @return
     */
    private Map<String, Object> getParamNameAndValue(ProceedingJoinPoint joinPoint) {
        Map<String, Object> param = new HashMap<>(8);
        Object[] paramValues = joinPoint.getArgs();
        String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();

        for (int i = 0; i < paramNames.length; i++) {
            param.put(paramNames[i], paramValues[i]);
        }
        return param;
    }

}
