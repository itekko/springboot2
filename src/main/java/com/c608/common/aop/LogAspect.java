package com.c608.common.aop;

import com.c608.common.annotation.Log;
import com.c608.common.utils.HttpContextUtils;
import com.c608.common.utils.IPUtils;
import com.c608.common.utils.JSONUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;


/**
 * 日志切面类
 */
@Component
@Aspect
public class LogAspect {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("@annotation(com.c608.common.annotation.Log)")
    public void logPointCut(){

    }

    /**
     * @param joinPoint
     * @return
     * @throws Throwable
     * 步骤如下：
     *   1.获取方法运行时间
     *   2.保存日志（暂时打印日志）
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Long beginTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        Long executeTime = System.currentTimeMillis() - beginTime;
        saveLog(joinPoint,executeTime);
        return result;
    }

   /* @After("logPointCut()")
    public void after() throws Throwable {
       log.info("after执行");
    }

    @Before("logPointCut()")
    public void before() throws Throwable {
        log.info("before");
    }*/

    private void saveLog(ProceedingJoinPoint joinPoint,Long executeTime) {{
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log syslog = method.getAnnotation(Log.class);
        if (syslog != null) {
            // 注解上的描述
            log.info("注解描述:{}",syslog.value());
        }
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.info("方法信息({}.{})",className,methodName);
        // 请求的参数JSONUtils
        Object[] args = joinPoint.getArgs();
        try {
            String params = JSONUtils.beanToJson(args[0]).substring(0, 4999);
            log.info("参数:{}",params);
        } catch (Exception e) {

        }
        // 获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        // 设置IP地址
        log.info("IP地址:" + IPUtils.getIpAddr(request));
        log.info("执行时间(毫秒):" + executeTime);
    }


    }


}
