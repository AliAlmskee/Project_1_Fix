package com.project1.seeder;

import com.project1.category.Category;
import com.project1.category.CategoryRepository;
import com.project1.skill.Skill;
import com.project1.skill.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SkillSeeder {

    private final SkillRepository skillRepository;
    private final CategoryRepository categoryRepository;


    public void seed() {
        if (skillRepository.count() == 0) {
            List<Skill> skills = Arrays.asList(
                    Skill.builder().name("خطة عمل").category(getCategoryById(1L)).build(),
                    Skill.builder().name("إدارة المشاريع").category(getCategoryById(1L)).build(),
                    Skill.builder().name("إدخال بيانات").category(getCategoryById(7L)).build(),
                    Skill.builder().name("مايكروسوفت أوفيس").category(getCategoryById(7L)).build(),
                    Skill.builder().name("خدمة العملاء").category(getCategoryById(7L)).build(),
                    Skill.builder().name("البحث والتطوير").category(getCategoryById(5L)).build(),
                    Skill.builder().name("كتابة المحتوى").category(getCategoryById(7L)).build(),
                    Skill.builder().name("البحث على الإنترنت").category(getCategoryById(5L)).build(),
                    Skill.builder().name("مونتاج فيديو").category(getCategoryById(4L)).build(),
                    Skill.builder().name("كتابة التقارير").category(getCategoryById(6L)).build()
            );

            skillRepository.saveAll(skills);
        }
    }

    private Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found for id: " + categoryId));
    }
}
