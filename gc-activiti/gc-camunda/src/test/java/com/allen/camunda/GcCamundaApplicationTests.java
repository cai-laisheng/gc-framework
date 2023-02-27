package com.allen.camunda;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class GcCamundaApplicationTests {

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	/**
	 * 启动流程的案例
	 */
	@Test
	public void startFlow(){
		// 部署流程
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceById("3756193d-b67d-11ed-9d35-de3a4572ab29 ");
		// 部署的流程实例的相关信息
		System.out.println("processInstance.getId() = " + processInstance.getId());
		System.out.println("processInstance.getProcessDefinitionId() = " + processInstance.getProcessDefinitionId());
	}

	/**
	 * 查询任务
	 *    待办
	 *
	 *  流程定义ID:processDefinition : 我们部署流程的时候会，每一个流程都会产生一个流程定义ID
	 *  流程实例ID:processInstance ：我们启动流程实例的时候，会产生一个流程实例ID
	 */
	@Test
	public void queryTask(){
		List<Task> list = taskService.createTaskQuery()
				//.processInstanceId("eff78817-2e58-11ed-aa3f-c03c59ad2248")
				.taskAssignee("demo")
				.list();
		if(list != null && list.size() > 0){
			for (Task task : list) {
				System.out.println("task.getId() = " + task.getId());
				System.out.println("task.getAssignee() = " + task.getAssignee());
			}
		}
	}

	/**
	 * 完成任务
	 */
	@Test
	public void completeTask(){
		// 根据用户找到关联的Task
		Task task = taskService.createTaskQuery()
				//.processInstanceId("eff78817-2e58-11ed-aa3f-c03c59ad2248")
				.taskAssignee("demo")
				.singleResult();
		if(task != null ){
			taskService.complete(task.getId());
			System.out.println("任务审批完成...");
		}
	}
}
