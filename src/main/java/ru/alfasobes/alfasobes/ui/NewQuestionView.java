package ru.alfasobes.alfasobes.ui;


import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.pekka.WysiwygE;
import ru.alfasobes.alfasobes.dao.QuestionRepository;
import ru.alfasobes.alfasobes.model.Question;
import ru.alfasobes.alfasobes.util.Const;

import javax.annotation.PostConstruct;

@Route(value = Const.NEW_QUESTION_PAGE,layout = View.class)
@StyleSheet("a.css")
@Component
@UIScope
public class NewQuestionView extends VerticalLayout {

    WysiwygE wysiwygE = new WysiwygE("300px", "800px");
    TextField question = new TextField("Как звучит вопрос?");
    TextField categories = new TextField("категории");
    Button save = new Button("СОХРАНИТЬ");

    Binder<Question> binder = new Binder<>();
    Question q = new Question();

    @Autowired
    private QuestionRepository repository;

    @PostConstruct
    public void postConstruct(){
        setHeight("800px");
        setWidth("1280px");
        setClassName("new-question-view");

        add(question,categories);
        add(wysiwygE);
        add(save);

        binder.forField(question)
                .withValidator(simpleValidator())
                .bind(Question::getQuestion,Question::setQuestion);

        binder.forField(categories)
                .withValidator(simpleValidator())
                .bind(Question::getCategories,Question::setCategories);

        binder.bind(wysiwygE,Question::getHint,Question::setHint);

        wysiwygE.addValueChangeListener(
                (HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<WysiwygE, String>>)
                        wysiwygEStringComponentValueChangeEvent -> System.out.println(wysiwygEStringComponentValueChangeEvent.getValue()));

        save.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            try {
                Question bean = new Question();
                binder.writeBean(bean);
                repository.save(bean);
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        });

    }

    private Validator<String> simpleValidator() {
        return (Validator<String>) (s, valueContext) -> StringUtils.isBlank(s)
                ? ValidationResult.error("введите значение")
                : ValidationResult.ok();
    }

}
