package com.project1.seeder;

import com.project1.category.Category;
import com.project1.category.CategoryRepository;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CategorySeeder {
    private final CategoryRepository categoryRepository;

    public void seed() {
        if (categoryRepository.count() == 0) {
            List<Category> categories = Arrays.asList(
                    Category.builder().name("أعمال وخدمات استشارية").build(),
                    Category.builder().name("برمجة، تطوير المواقع والتطبيقات").build(),
                    Category.builder().name("هندسة، عمارة وتصميم داخلي").build(),
                    Category.builder().name("تصميم، فيديو وصوتيات").build(),
                    Category.builder().name("تسويق إلكتروني ومبيعات").build(),
                    Category.builder().name("كتابة، تحرير، ترجمة ولغات").build(),
                    Category.builder().name("دعم، مساعدة وإدخال بيانات").build(),
                    Category.builder().name("تدريب وتعليم عن بعد").build()
            );
            categoryRepository.saveAll(categories);
        }
    }
}
