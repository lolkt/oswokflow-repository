/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.workflow.spi.hibernate;

import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.config.Configuration;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.loader.WorkflowFactory;
import com.opensymphony.workflow.spi.AbstractFunctionalWorkflowTest;
import com.opensymphony.workflow.util.DatabaseHelper;

import org.springframework.beans.factory.xml.XmlBeanFactory;

import org.springframework.core.io.ClassPathResource;


/**
 * White Box semi-functional test case that uses Spring's managed Hibernate as Store
 *
 * @author Luca Masini (l.masini@infogroup.it)
 */
public class SpringHibernateLoaderFunctionalWorkflowDisabled extends AbstractFunctionalWorkflowTest {
    //~ Constructors ///////////////////////////////////////////////////////////

    public SpringHibernateLoaderFunctionalWorkflowDisabled(String s) {
        super(s);
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    protected void setUp() throws Exception {
        super.setUp();
        DatabaseHelper.runScript("", "jdbc/CreateDS");

        XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("osworkflow-spring-hibernate-loader.xml"));
        WorkflowFactory oldFactory = (WorkflowFactory) beanFactory.getBean("xmlWorkflowFactory");
        WorkflowDescriptor wfd = oldFactory.getWorkflow(getWorkflowName());

        WorkflowFactory newFactory = (WorkflowFactory) beanFactory.getBean("workflowFactory");
        newFactory.saveWorkflow(getWorkflowName(), wfd, true);

        workflow = (Workflow) beanFactory.getBean("workflow");
        workflow.setConfiguration((Configuration) beanFactory.getBean("osworkflowConfiguration"));
    }

    protected String getWorkflowName() {
        return "example";
    }

    protected void tearDown() throws Exception {
        DatabaseHelper.runScript(getClass().getResource("/scripts/jdbc/dropschema.sql"), "jdbc/DefaultDS");
    }
}
