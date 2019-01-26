package ru.alfasobes.alfasobes.ui;


import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.pekka.WysiwygE;
import ru.alfasobes.alfasobes.dao.QuestionRepository;
import ru.alfasobes.alfasobes.model.Question;
import ru.alfasobes.alfasobes.util.Const;
import ru.alfasobes.alfasobes.util.VaadinUtils;

import javax.annotation.PostConstruct;

@Route(value = Const.NEW_QUESTION_PAGE, layout = MainView.class)
@StyleSheet(Const.STYLES)
@Component
@UIScope
public class NewQuestionView extends FormLayout {

    TextField question = new TextField("Как звучит вопрос?");
    TextField categories = new TextField("категории");
    Button save = new Button("СОХРАНИТЬ");

    TextArea textArea = new TextArea("пояснение для интервьювера");

    Binder<Question> binder = new Binder<>();
    Question q = new Question();

    @Autowired
    private QuestionRepository repository;

    @PostConstruct
    public void postConstruct() {
        setClassName("new-question");

        add(question, categories);
        add(textArea);
        add(save);

        save.addThemeVariants(ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_PRIMARY);

        binder.forField(question)
                .withValidator(VaadinUtils.simpleValidator())
                .bind(Question::getQuestion, Question::setQuestion);

        binder.forField(categories)
                .withValidator(VaadinUtils.simpleValidator())
                .bind(Question::getCategories, Question::setCategories);

        binder.bind(textArea, Question::getHint, Question::setHint);

        save.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            try {
                Question bean = new Question();
                binder.writeBean(bean);
                repository.save(bean);
                Notification.show("Сохранено",1000,Notification.Position.MIDDLE);
                clear();
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        });
    }

    public void clear(){
        binder.readBean(null);
    }

}
