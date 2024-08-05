package com.project1.project.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.ReadOnlyProperty;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectWithOfferCount extends Project{
    @ReadOnlyProperty
    private Integer offerCount;
}
