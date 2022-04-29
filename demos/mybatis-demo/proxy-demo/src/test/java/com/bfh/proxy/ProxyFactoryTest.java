package com.bfh.proxy;

import com.bfh.service.Service;
import com.bfh.service.impl.SuperStarLiu;
import com.bfh.service.impl.SuperStarZhou;
import org.junit.Test;

import java.lang.reflect.Proxy;

/**
 * @author benfeihu
 */
public class ProxyFactoryTest {
    @Test
    public void testJDK() {
        ProxyFactory factory = new ProxyFactory(new SuperStarZhou());
        Service agent = (Service) factory.getAgent();
        // agent.sing();
        String s = agent.show(23);
        System.out.println(s);
        System.out.println(agent.getClass());

    }

}