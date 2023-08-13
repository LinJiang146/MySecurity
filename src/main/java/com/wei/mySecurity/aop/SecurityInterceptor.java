package com.wei.mySecurity.aop;

import com.wei.mySecurity.Annotation.AuthenticatedByPermission;
import com.wei.mySecurity.SecurityManager;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class SecurityInterceptor implements MethodInterceptor {

    /**
     *
     * @param obj 表示要进行增强的对象
     * @param method 表示拦截的方法
     * @param objects 数组表示参数列表，基本数据类型需要传入其包装类型，如int-->Integer、long-Long、double-->Double
     * @param methodProxy 表示对方法的代理，invokeSuper方法表示对被代理对象方法的调用
     * @return 执行结果
     * @throws Throwable 异常
     */

    @Autowired
    private SecurityManager securityManager;


    @Override
    public Object intercept(Object obj, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        boolean pass = securityManager.AuthenticatedInClass(method.getDeclaringClass());
        pass = pass && securityManager.AuthenticatedInMethod(method);

        // 注意这里是调用invokeSuper而不是invoke，否则死循环;
        // methodProxy.invokeSuper执行的是原始类的方法;
        // method.invoke执行的是子类的方法;
        if (pass) return methodProxy.invokeSuper(obj, objects);

        return null;
    }
}