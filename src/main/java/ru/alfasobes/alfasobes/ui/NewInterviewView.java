package ru.alfasobes.alfasobes.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.alfasobes.alfasobes.dao.CandidateRepository;
import ru.alfasobes.alfasobes.dao.InterviewRepository;
import ru.alfasobes.alfasobes.dao.QuestionRepository;
import ru.alfasobes.alfasobes.model.Candidate;
import ru.alfasobes.alfasobes.model.Interview;
import ru.alfasobes.alfasobes.model.InterviewQuestion;
import ru.alfasobes.alfasobes.model.Question;
import ru.alfasobes.alfasobes.util.Const;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Route(value = Const.COMPOSE_INTERVIEW_PAGE, layout = MainView.class)
@UIScope
@StyleSheet(Const.STYLES)
public class NewInterviewView extends VerticalLayout {

    private TextField name = new TextField("ФИО кандидата");
    private Grid<Question> grid = new Grid<>();
    private Button save = new Button("СОХРАНИТЬ");

    @Autowired
    private QuestionRepository questionRepository;

    private Binder<Candidate> binder = new Binder<>();

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private InterviewRepository interviewRepository;

    @PostConstruct
    public void postConstruct(){

        addAttachListener(new ComponentEventListener<AttachEvent>() {
            @Override
            public void onComponentEvent(AttachEvent attachEvent) {
                fillGrid();
            }
        });

        initGrid();
        fillGrid();

        binder.forField(name).bind(Candidate::getName,Candidate::setName);

        save.addThemeVariants(ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_PRIMARY);

        save.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            try {
                Candidate candidate = new Candidate();
                binder.writeBean(candidate);
                Set<Question> selectedItems = grid.getSelectedItems();
                Interview interview = new Interview();
                for (Question q : selectedItems){
                    interview.getInterviewQuestions().add(new InterviewQuestion(q));
                }
                candidate.addInterview(interview);
                interview.setCandidate(candidate);
                candidateRepository.save(candidate);
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        });

        add(name, grid, save);

    }

    private void initGrid() {
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        grid.addColumn(Question::getQuestion).setHeader("вопрос");
        grid.addColumn(Question::getCategories).setHeader("категории");
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
    }

    private void fillGrid() {
        Iterable<Question> all = questionRepository.findAll();
        List<Question> questions = new ArrayList<>();
        all.forEach(questions::add);
        grid.setItems(questions);
    }

}
