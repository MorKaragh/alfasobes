package ru.alfasobes.alfasobes.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Route(value = "interview",layout = View.class)
@UIScope
public class InterviewView extends VerticalLayout {

    Button goodAns = new Button("Хороший ответ");
    Button moderateAns = new Button("Удовлетворительный ответ");
    Button badAns = new Button("Нет ответа");

    @PostConstruct
    public void postConstruct(){
        add(goodAns,moderateAns,badAns);
    }

}
