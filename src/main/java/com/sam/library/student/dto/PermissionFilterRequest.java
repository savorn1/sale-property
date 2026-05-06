package com.sam.library.student.dto;

import org.springdoc.core.annotations.ParameterObject;

@ParameterObject
public class PermissionFilterRequest {

    private String name;
    private String action;
    private String module;
    private String sortBy = "createdAt";
    private String sortOrder = "desc";
    private int page = 1;
    private int size = 10;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }

    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }

    public String getSortOrder() { return sortOrder; }
    public void setSortOrder(String sortOrder) { this.sortOrder = sortOrder; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
}
