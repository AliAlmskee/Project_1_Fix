package com.project1.project.data;

import lombok.*;
import org.springframework.data.annotation.ReadOnlyProperty;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectWithOfferCount extends Project{
    @ReadOnlyProperty
    private Integer offerCount;
}
