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
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.alfasobes.alfasobes.dao.QuestionRepository;
import ru.alfasobes.alfasobes.model.Question;
import ru.alfasobes.alfasobes.util.Const;
import ru.alfasobes.alfasobes.util.VaadinUtils;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;


@StyleSheet(Const.STYLES)
@Component
@Scope("prototype")
public class QuestionForm  extends FormLayout {

    private TextField questionText = new TextField("Как звучит вопрос?");
    private TextField categories = new TextField("категории");
    private Button save = new Button("СОХРАНИТЬ");

    private TextArea textArea = new TextArea("пояснение для интервьювера");

    private Binder<Question> binder = new Binder<>();

    @Autowired
    private QuestionRepository repository;
    private Question question;
    private AfterSaveListener afterSaveListener;

    @PostConstruct
    public void postConstruct() {

        add(questionText, categories);
        add(textArea);
        add(save);

        categories.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>>) textFieldStringComponentValueChangeEvent -> {
            String res = "";
            String value = textFieldStringComponentValueChangeEvent.getValue().toLowerCase();
            if (StringUtils.isNoneBlank(value)) {
                String[] cats = value.split(" ");
                Arrays.sort(cats);
                res = String.join(" ", cats);
            }
            textFieldStringComponentValueChangeEvent.getSource().setValue(res);
        });

        save.addThemeVariants(ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_PRIMARY);

        binder.forField(questionText)
                .withValidator(VaadinUtils.simpleValidator())
                .bind(Question::getQuestion, Question::setQuestion);

        binder.forField(categories)
                .withValidator(VaadinUtils.simpleValidator())
                .bind(Question::getCategories, Question::setCategories);

        binder.bind(textArea, Question::getHint, Question::setHint);

        save.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            save();
        });
    }

    private void save() {
        try {
            if (question == null) {
                question = new Question();
            }
            binder.writeBean(question);
            question.setDateAdd(LocalDateTime.now());
            repository.save(question);
            Notification.show("Сохранено", 1000, Notification.Position.MIDDLE);
            if (afterSaveListener != null){
                afterSaveListener.afterSave();
            }
        } catch (ValidationException e) {
            //молчим, ui сам отобразит ошибку
        }
    }

    public void addAfterSaveListener(AfterSaveListener listener){
        this.afterSaveListener = listener;
    }

    public void setQuestion(Question question) {
        this.question = question;
        binder.setBean(question);
    }

    public void clear() {
        question = null;
        binder.readBean(null);
    }

    public interface AfterSaveListener{
        void afterSave();
    }

}
