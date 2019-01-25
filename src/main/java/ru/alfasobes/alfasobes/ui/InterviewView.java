package ru.alfasobes.alfasobes.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import ru.alfasobes.alfasobes.util.Const;

import javax.annotation.PostConstruct;

@Route(value = "interview",layout = MainView.class)
@UIScope
@StyleSheet(Const.STYLES)
public class InterviewView extends VerticalLayout {

    Button goodAns = new Button("Хороший ответ");
    Button moderateAns = new Button("Удовлетворительный ответ");
    Button badAns = new Button("Нет ответа");

    @PostConstruct
    public void postConstruct(){
        add(goodAns,moderateAns,badAns);
    }

}
