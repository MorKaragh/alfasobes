package ru.alfasobes.alfasobes.ui;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.alfasobes.alfasobes.dao.InterviewQuestionRepository;
import ru.alfasobes.alfasobes.dao.InterviewRepository;
import ru.alfasobes.alfasobes.model.Interview;
import ru.alfasobes.alfasobes.model.InterviewQuestion;
import ru.alfasobes.alfasobes.util.Const;

@Component
@Scope("prototype")
@StyleSheet(Const.STYLES)
public class InterviewDialog extends VerticalLayout {

    private VerticalLayout questionLayout = new VerticalLayout();

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private InterviewQuestionRepository interviewQuestionRepository;


    private Button backToJournal = new Button("Назад в журнал");
    private Button finish = new Button("Завершить");

    private Button goodAns = new Button("Хороший ответ");
    private Button moderateAns = new Button("Более-менее");
    private Button badAns = new Button("Нет ответа");

    private Button next = new Button(new Icon(VaadinIcon.ARROW_FORWARD));
    private Button prev = new Button(new Icon(VaadinIcon.ARROW_BACKWARD));

    private Interview interview;
    private int currentQuestionIndex = 0;


    InterviewDialog() {
        buildLayout();
        setListeners();
    }


    private void buildLayout() {
        HorizontalLayout upperMenu = new HorizontalLayout();
        upperMenu.add(backToJournal, finish);

        backToJournal.addThemeVariants(ButtonVariant.LUMO_LARGE);
        backToJournal.setIcon(new Icon(VaadinIcon.BACKSPACE));
        finish.addThemeVariants(ButtonVariant.LUMO_LARGE);

        goodAns.addThemeVariants(ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        moderateAns.addThemeVariants(ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_PRIMARY);
        badAns.addThemeVariants(ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        goodAns.setWidth("200px");
        badAns.setWidth("200px");
        moderateAns.setWidth("200px");

        next.addThemeVariants(ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST);
        prev.addThemeVariants(ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.add(prev,goodAns, moderateAns, badAns,next);

        questionLayout.setHeight("90%");
        add(upperMenu,questionLayout,buttons);
    }

    private void setListeners() {
        next.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            if (currentQuestionIndex < interview.getInterviewQuestions().size()-1) {
                displayQuestion(interview.getInterviewQuestions().get(++currentQuestionIndex));
            }
        });

        prev.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            if (currentQuestionIndex > 0) {
                displayQuestion(interview.getInterviewQuestions().get(--currentQuestionIndex));
            }
        });

        goodAns.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {

        });
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
        displayQuestion(interview.getInterviewQuestions().get(currentQuestionIndex));
    }

    private void displayQuestion(InterviewQuestion nextUnansweredQuestion) {
        questionLayout.removeAll();
        questionLayout.add(new QuestionBlock(nextUnansweredQuestion));
    }


    class QuestionBlock extends Html {
        QuestionBlock(InterviewQuestion question){
            super("<div><p><H1>"+question.getQuestion().getQuestion()+"</H1></p><p>"+question.getQuestion().getHint()+"</p></div>");
        }
    }

}
