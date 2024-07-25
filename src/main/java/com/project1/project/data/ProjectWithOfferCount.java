package com.project1.project.data;

import org.springframework.data.annotation.ReadOnlyProperty;

public class ProjectWithOfferCount extends Project{
    @ReadOnlyProperty
    private Integer offerCount;
}
