package ru.alfasobes.alfasobes.ui.components;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.vaadin.flow.component.Key.SPACE;

@Component
@UIScope
public class CategoryFieldPresenter {
    private CategoryField categoryField;

    private List<String> values = new ArrayList<>();

    public VerticalLayout buildLayout(){
        categoryField = new CategoryField();
        TextField field = categoryField.textField;
        field.addKeyDownListener(SPACE, inputEvent -> {
            if (!field.isEmpty()) {
                String value = field.getValue();

                values.add(value);
                categoryField.tagListLayout.add(new CustomTag(value));
                field.clear();
            }
        });
        return categoryField;
    }

    public List<String> gatherData(){
        return values;
    }
}
