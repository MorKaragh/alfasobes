package ru.alfasobes.alfasobes.util;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import org.apache.commons.lang3.StringUtils;

public class VaadinUtils {

    public static Validator<String> simpleValidator() {
        return (Validator<String>) (s, valueContext) -> StringUtils.isBlank(s)
                ? ValidationResult.error("введите значение")
                : ValidationResult.ok();
    }

}
