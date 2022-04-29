package com.bfh.proxy;

import com.bfh.service.Service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author benfeihu
 */
public class ProxyFactory {

    Service target;

    public ProxyFactory(Service target) {
        this.target = target;
    }

    // 返回动态代理对象
    public Object getAgent() {
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new InvocationHandler() {
                    /**
                     * @param proxy 创建好的代理对象
                     * @param method 目标方法
                     * @param args 目标方法的参数
                     * @return
                     * @throws Throwable
                     */
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        // 代理功能
                        System.out.println("预定时间...");
                        // 代理功能
                        System.out.println("预定场地...");
                        // 主业务功能
                        Object result = method.invoke(target, args);
                        // 代理功能
                        System.out.println("费用结算...");
                        return result;
                    }
                }
        );
    }
}
