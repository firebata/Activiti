package org.activiti.spring.conformance.set0;

import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.process.runtime.conf.ProcessRuntimeConfiguration;
import org.activiti.api.process.runtime.events.listener.ProcessRuntimeEventListener;
import org.activiti.api.runtime.shared.events.VariableEventListener;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.spring.conformance.util.RuntimeTestConfiguration;
import org.activiti.spring.conformance.util.security.SecurityUtil;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ConformanceBasicProcessRuntimeTest {


    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private SecurityUtil securityUtil;

    @Test
    public void shouldGetConfiguration() {
        securityUtil.logInAs("user1");
        //when
        ProcessRuntimeConfiguration configuration = processRuntime.configuration();
        //then
        assertThat(configuration).isNotNull();
        //when
        List<ProcessRuntimeEventListener<?>> processRuntimeEventListeners = configuration.processEventListeners();
        List<VariableEventListener<?>> variableEventListeners = configuration.variableEventListeners();
        //then
        assertThat(processRuntimeEventListeners).hasSize(11);
        assertThat(variableEventListeners).hasSize(3);

    }

    @Test
    public void shouldProcessDefinitions() {
        securityUtil.logInAs("user1");

        Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 50));

        List<ProcessDefinition> processDefinitions = processDefinitionPage.getContent();
        assertThat(processDefinitions).extracting(ProcessDefinition::getName).containsOnly(
                "Process Information",
                "Process with Generic  BPMN Task",
                "UserTask with no User or Group Assignment"
        );

    }

    @Test
    public void shouldProcessDefinitionsMetaData() {
        securityUtil.logInAs("user1");

        Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 50));

        List<ProcessDefinition> processDefinitions = processDefinitionPage.getContent();
        assertThat(processDefinitions).extracting(ProcessDefinition::getName).containsOnly(
                "Process Information",
                "Process with Generic  BPMN Task",
                "UserTask with no User or Group Assignment"
        );

    }

    @After
    public void cleanUp() {
        RuntimeTestConfiguration.collectedEvents.clear();
    }



}
