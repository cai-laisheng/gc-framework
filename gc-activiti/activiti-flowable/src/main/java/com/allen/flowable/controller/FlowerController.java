package com.allen.flowable.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author xuguocai
 * @Date 16:27 2023/2/23
 **/
@Slf4j
@RestController
@RequestMapping("/flower")
public class FlowerController {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private ProcessEngine processEngine;


    /**
     * 启动流程()
     * @param roleId    角色id（我这里配置候选用户,这里就是用roleId）
     */
    @PostMapping(value = "add")
    public String addExpense(String roleId) {
        //启动流程
        HashMap<String, Object> map = new HashMap<>(4);

        //name="客服代表" flowable:candidateGroups="${customerServiceId}"

        map.put("customerServiceId", roleId);

        //<process id="adviceApply" name="投诉建议" isExecutable="true">
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("adviceApply", map);
        return "提交成功.流程Id为：" + processInstance.getId();
    }

    /**
     * 流转
     *
     * @param processInstanceId 流程id
     */
    @PutMapping(value = "apply")
    public String apply(String processInstanceId,Long userId,boolean isPass) {
        // Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        //查询当前办理人的任务ID
        Task task = taskService.createTaskQuery()
                //使用流程实例ID
                .processInstanceId(processInstanceId)
                //任务办理人
                .singleResult();
        if (task == null) {
            throw new RuntimeException("流程不存在");
        }
        //通过审核
        HashMap<String, Object> map = new HashMap<>();
        map.put("taskUser", userId);
        String pass = "NO";
        if(isPass){
            pass = "YES";
        }
        map.put("finishFlag", pass);
        taskService.complete(task.getId(), map);
        return "processed ok!";
    }

    /**
     * 生成流程图
     *
     * @param processId 任务ID
     */
    @GetMapping(value = "processDiagram")
    public void genProcessDiagram(HttpServletResponse httpServletResponse, String processId) throws Exception {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();

        //流程走完的不显示图
        if (pi == null) {
            return;
        }
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        String InstanceId = task.getProcessInstanceId();
        List<Execution> executions = runtimeService
                .createExecutionQuery()
                .processInstanceId(InstanceId)
                .list();

        //得到正在执行的Activity的Id
        List<String> activityIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();
        for (Execution exe : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
            activityIds.addAll(ids);
        }

        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
        ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engconf.getProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", activityIds, flows,
                engconf.getActivityFontName(), engconf.getLabelFontName(), engconf.getAnnotationFontName(),
                engconf.getClassLoader(), 1.0,true);
        OutputStream out = null;
        byte[] buf = new byte[1024];
        int legth = 0;
        try {
            out = httpServletResponse.getOutputStream();
            while ((legth = in.read(buf)) != -1) {
                out.write(buf, 0, legth);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

}
