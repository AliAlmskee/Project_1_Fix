package com.project1.projectProgress;

import com.project1.auditing.ApplicationAuditAware;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectProgressService {
    private final ProjectProgressMapper projectProgressMapper;
    private final ProjectProgressRepository projectProgressRepository;
    private final ApplicationAuditAware auditAware;


}
