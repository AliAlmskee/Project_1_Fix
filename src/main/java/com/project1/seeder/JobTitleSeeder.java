package com.project1.seeder;

import com.project1.jobTitle.JobTitle;
import com.project1.jobTitle.JobTitleRepository;
import com.project1.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JobTitleSeeder {
    private final JobTitleRepository jobTitleRepository;

    public void seed()
    {
        if (jobTitleRepository.count() == 0) {
            if (jobTitleRepository.count() == 0) {
                List<JobTitle> jobTitles = Arrays.asList(
                        JobTitle.builder().title("مطور ويب").build(),
                        JobTitle.builder().title("مصمم جرافيك").build(),
                        JobTitle.builder().title("كاتب محتوى").build(),
                        JobTitle.builder().title("أخصائي تحسين محركات البحث").build(),
                        JobTitle.builder().title("مطور تطبيقات الجوال").build(),
                        JobTitle.builder().title("محلل بيانات").build(),
                        JobTitle.builder().title("مدير مشروع").build(),
                        JobTitle.builder().title("مصمم واجهات المستخدم وتجربة المستخدم").build(),
                        JobTitle.builder().title("مهندس برمجيات").build(),
                        JobTitle.builder().title("أخصائي تسويق رقمي").build()
                );


                jobTitleRepository.saveAll(jobTitles);
            }
        }

    }
}
