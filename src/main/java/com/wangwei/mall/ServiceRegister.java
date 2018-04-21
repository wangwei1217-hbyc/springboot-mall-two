package com.wangwei.mall;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 服务的注册
 */
@Component
public class ServiceRegister implements ApplicationRunner{

    @Value("${zookeeper.address}")
    private String zooAddress;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(zooAddress, new RetryOneTime(1000));

        client.start();
        client.blockUntilConnected();

        ServiceInstance<Object> instance = ServiceInstance.builder().name("mall").address("192.168.171.1").port(8181).build();

        ServiceDiscovery<Object> serviceDiscovery = ServiceDiscoveryBuilder.builder(Object.class).client(client).basePath("/soa").build();

        serviceDiscovery.registerService(instance);
        serviceDiscovery.start();

        System.out.println("mall-two register successful");
    }
}
