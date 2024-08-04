package validation;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueValidator implements ConstraintValidator<Unique, Object> {

    private Class<?> entity;
    private String fieldName;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void initialize(Unique unique) {
        this.entity = unique.entity();
        this.fieldName = unique.fieldName();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String query = String.format("SELECT COUNT(e) FROM %s e WHERE e.%s = :value", entity.getSimpleName(), fieldName);
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("value", value)
                .getSingleResult();

        return count == 0;
    }
}