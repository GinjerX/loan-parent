package com.ps.config;

import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;

public class ZkPropertySourceFactory implements PropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {

        ResourcePropertySource zkSource = new ResourcePropertySource(resource);
        zkSource.getProperty("datasource.username");

        System.out.println("name: " + name);
        System.out.println(resource.getResource().getFile());

        return new ResourcePropertySource(resource);
    }
}
