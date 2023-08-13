package com.wei.mySecurity.aop;

import com.wei.mySecurity.Annotation.AuthenticatedByPermission;
import com.wei.mySecurity.Annotation.AuthenticatedByRole;
import net.sf.cglib.proxy.Enhancer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class SecurityBeanProcessor implements BeanPostProcessor {

    @Autowired
    private SecurityInterceptor securityInterceptor;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        //判断是否需要代理
        boolean needProxy = false;
        needProxy = needProxy || bean.getClass().isAnnotationPresent(AuthenticatedByRole.class);
        needProxy = needProxy || bean.getClass().isAnnotationPresent(AuthenticatedByPermission.class);
        if (needProxy==false){
            Method[] methods = bean.getClass().getMethods();
            for (Method method : methods) {
                needProxy = needProxy || method.isAnnotationPresent(AuthenticatedByRole.class);
                needProxy = needProxy || method.isAnnotationPresent(AuthenticatedByPermission.class);
                if (needProxy) break;
            }
        }

        if (needProxy){
            // 通过CGLIB动态代理获取代理对象的过程
            // 创建Enhancer对象，类似于JDK动态代理的Proxy类
            Enhancer enhancer = new Enhancer();
            // 设置目标类的字节码文件
            enhancer.setSuperclass(bean.getClass());
            // 设置回调函数
            enhancer.setCallback(securityInterceptor);
            // create方法正式创建代理类
            Object proxy = enhancer.create();

            return BeanPostProcessor.super.postProcessAfterInitialization(proxy, beanName);
        }

        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
