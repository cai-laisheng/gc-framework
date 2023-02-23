package com.allen.flowable;

import jakarta.annotation.Resource;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.DeploymentBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStream;

/**
 * @Author xuguocai
 * @Date 16:21 2023/2/23
 **/
@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Resource
    private RepositoryService repositoryService;

    @Override
    public void run(String... args)  {
        InputStream inputStream = this.getClass().getResourceAsStream("/process/投诉建议.bpmn20.xml");
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addInputStream("投诉建议.bpmn20.xml", inputStream);
        deploymentBuilder.deploy();
    }

}
