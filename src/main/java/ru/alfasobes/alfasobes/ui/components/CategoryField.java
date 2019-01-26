package ru.alfasobes.alfasobes.ui.components;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;


public class CategoryField extends VerticalLayout {
    TextField textField = new TextField("Категории");
    VerticalLayout tagListLayout = new VerticalLayout();

    public CategoryField() {
        add(textField);
        add(tagListLayout);
    }
}
