package ru.alfasobes.alfasobes.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@UIScope
public class CategoryPicker extends VerticalLayout {

    @PostConstruct
    public void postConstruct(){

    }

}
