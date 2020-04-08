package cn.wanlong.batch.entity;

import java.io.Serializable;

public class JobEntity implements Serializable {
    private String entityId;

    private String entityName;

    private String entityGroup;

    private String cron;

    private String parameter;

    private String description;

    private String jobClass;

    private String jarPath;

    private String status;

    private static final long serialVersionUID = 1L;

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId == null ? null : entityId.trim();
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName == null ? null : entityName.trim();
    }

    public String getEntityGroup() {
        return entityGroup;
    }

    public void setEntityGroup(String entityGroup) {
        this.entityGroup = entityGroup == null ? null : entityGroup.trim();
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron == null ? null : cron.trim();
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter == null ? null : parameter.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass == null ? null : jobClass.trim();
    }

    public String getJarPath() {
        return jarPath;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath == null ? null : jarPath.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}