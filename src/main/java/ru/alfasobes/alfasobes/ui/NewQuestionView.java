package ru.alfasobes.alfasobes.ui;


import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.alfasobes.alfasobes.dao.QuestionRepository;
import ru.alfasobes.alfasobes.model.Question;
import ru.alfasobes.alfasobes.util.Const;
import ru.alfasobes.alfasobes.util.VaadinUtils;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;

@Route(value = Const.NEW_QUESTION_PAGE, layout = MainView.class)
@StyleSheet(Const.STYLES)
@Component
@UIScope
public class NewQuestionView extends VerticalLayout {


    @Autowired
    private ApplicationContext context;

    @PostConstruct
    public void postConstruct() {
        QuestionForm form = context.getBean(QuestionForm.class);

        setClassName("new-question");
        form.setClassName("new-question");
        form.addAfterSaveListener(new QuestionForm.AfterSaveListener() {
            @Override
            public void afterSave() {
                form.clear();
            }
        });
        add(form);
    }


}
