package org.example;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;

public class AnnotationValidator implements ConstraintValidator<ValidatingItem,Item[]> {
    @Override
    public void initialize(ValidatingItem constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Item[] item, ConstraintValidatorContext constraintValidatorContext) {
        ArrayList<Boolean> arrayList=new ArrayList<>();
        for(Item item1:item){
            arrayList.add(item1.name!=null && item1.cost!=0 && !(item1.cost>1000000) && item1.name.length()<32);

        }
        return !arrayList.contains(false);
    }
}
