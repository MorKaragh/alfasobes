package ru.alfasobes.alfasobes.ui;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
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
import ru.alfasobes.alfasobes.model.InterviewAnswer;
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
        buttons.add(prev, goodAns, moderateAns, badAns, next);

        add(upperMenu, questionLayout, buttons);
    }

    private void setListeners() {
        next.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            displayNextQuestion();
        });

        prev.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            displayPrevQuestion();
        });

        goodAns.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            acceptAnswer(InterviewAnswer.GOOD);
        });

        badAns.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            acceptAnswer(InterviewAnswer.BAD);
        });

        moderateAns.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            acceptAnswer(InterviewAnswer.MODERATE);
        });

        backToJournal.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                UI.getCurrent().getPage().reload();
            }
        });
    }

    private void acceptAnswer(InterviewAnswer ans) {
        getCurrentQuestion().setAnswer(ans);
        interviewQuestionRepository.save(getCurrentQuestion());
        displayNextQuestion();
    }

    private void displayNextQuestion() {
        if (currentQuestionIndex < interview.getInterviewQuestions().size() - 1) {
            currentQuestionIndex++;
        }
        displayQuestion(getCurrentQuestion());
    }

    private void displayPrevQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
        }
        displayQuestion(getCurrentQuestion());
    }

    private InterviewQuestion getCurrentQuestion() {
        return interview.getInterviewQuestions().get(currentQuestionIndex);
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
        displayQuestion(getCurrentQuestion());
    }

    private void displayQuestion(InterviewQuestion question) {
        questionLayout.removeAll();
        questionLayout.add(new QuestionBlock(question));
    }


    class QuestionBlock extends VerticalLayout {
        QuestionBlock(InterviewQuestion question) {
            add(new Html("<H1>" + question.getQuestion().getQuestion() + "</H1>"));
            add(new Html("<div><p>" + question.getQuestion().getHint() + "</p></div>"));
            add(statusLabel(question));
        }

        private com.vaadin.flow.component.Component statusLabel(InterviewQuestion question) {
            String label = "<H2>-</H2>";
            if (question.getAnswer() != null)
                switch (question.getAnswer()) {
                    case BAD:
                        label = "<H2>дан <span style=\"color:red\">плохой</span> ответ</H2>";
                        break;
                    case GOOD:
                        label = "<H2>дан <span style=\"color:green\">хороший</span> ответ</H2>";
                        break;
                    case MODERATE:
                        label = "<H2>дан <span style=\"color:blue\">нормальный</span> ответ</H2>";
                        break;
                }
            return new Html(label);
        }
    }

}
