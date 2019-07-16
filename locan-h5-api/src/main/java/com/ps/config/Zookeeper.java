package com.ps.config;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Zookeeper {
    public void zookeeper() throws IOException, KeeperException, InterruptedException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("/D:/IDEA/config/config.properties"));

        String hostPort = "localhost:2181";
        ZooKeeper zooKeeper = new ZooKeeper(hostPort, 10000, null);

        for (Object o : properties.keySet()) {
            System.out.println("o:"+o);

            Stat exists = zooKeeper.exists("/zookeeper/" + o, null);
            if (exists == null) {
                zooKeeper.create("/zookeeper/" +o,properties.get(o).toString().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            zooKeeper.setData("/zookeeper/" +o,properties.get(o).toString().getBytes(), -1);
        }

        /*List<String> children = zooKeeper.getChildren("/zookeeper", null, null);
        System.out.println(children);
        for (String child : children) {
            zooKeeper.getData("/zookeeper/"+child, null, null);

            System.setProperty(child,"/zookeeper/"+child);
        }*/
    }
}
