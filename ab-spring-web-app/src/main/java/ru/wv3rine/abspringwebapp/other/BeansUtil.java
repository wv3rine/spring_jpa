package ru.wv3rine.abspringwebapp.other;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.util.Collection;

// https://itecnote.com/tecnote/java-copy-non-null-properties-from-one-object-to-another-using-beanutils-or-similar/
// Для копирования non-null полей. Насколько это вообще стоит того, считая используемую рефлексию? Вроде
// я использую это не в цикле и должно мало времени занимать

public class BeansUtil<T> {
    public T copyNonNullProperties(T target, T in) {
        if (in == null || target == null || target.getClass() != in.getClass()) return null;

        final BeanWrapper src = new BeanWrapperImpl(in);
        final BeanWrapper trg = new BeanWrapperImpl(target);

        for (final Field property : target.getClass().getDeclaredFields()) {
            Object providedObject = src.getPropertyValue(property.getName());
            if (providedObject != null && !(providedObject instanceof Collection<?>)) {
                trg.setPropertyValue(
                        property.getName(),
                        providedObject);
            }
        }
        return target;
    }
}