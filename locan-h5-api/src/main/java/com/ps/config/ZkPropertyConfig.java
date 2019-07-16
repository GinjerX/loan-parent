package com.ps.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "zk.properties", factory = ZkPropertySourceFactory.class)
public class ZkPropertyConfig {

}
