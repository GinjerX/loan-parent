package com.ps.config;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Configuration
public class ZKConfig {

    @PostConstruct
    public void process() throws IOException, KeeperException, InterruptedException {
        /*System.out.println("执行。。。。");
        System.setProperty("jdbc.url","192.168.3.44");
        System.setProperty("jdbc.username","username");*/
        String hostPort = "localhost:2181";
        ZooKeeper zooKeeper = new ZooKeeper(hostPort, 10000, null);

        List<String> children = zooKeeper.getChildren("/zookeeper", null, null);
        System.out.println(children);
        for (String child : children) {
            zooKeeper.getData("/zookeeper/"+child, null, null);

            System.setProperty(child,"/zookeeper/"+child);
        }
    }

}
