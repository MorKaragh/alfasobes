package ru.alfasobes.alfasobes.ui;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.alfasobes.alfasobes.dao.CandidateRepository;
import ru.alfasobes.alfasobes.dao.InterviewRepository;
import ru.alfasobes.alfasobes.dao.QuestionRepository;
import ru.alfasobes.alfasobes.model.Candidate;
import ru.alfasobes.alfasobes.model.Interview;
import ru.alfasobes.alfasobes.model.InterviewQuestion;
import ru.alfasobes.alfasobes.model.Question;
import ru.alfasobes.alfasobes.util.CategorySearcher;
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
    private TextField filter = new TextField("фильтр по категориям");
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
    public void postConstruct() {

        addAttachListener((ComponentEventListener<AttachEvent>) attachEvent -> fillGrid());

        initGrid();
        fillGrid();

        name.setWidth("500px");
        binder.forField(name).bind(Candidate::getName, Candidate::setName);

        filter.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>>) textFieldStringComponentValueChangeEvent -> {
            String searchString = textFieldStringComponentValueChangeEvent.getValue();
            processSearch(searchString);
        });

        save.addThemeVariants(ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_PRIMARY);

        save.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            save();
        });

        HorizontalLayout fields = new HorizontalLayout();
        fields.add(name, filter);
        add(fields, grid, save);

    }

    private void save() {
        try {
            Candidate candidate = new Candidate();
            binder.writeBean(candidate);
            Set<Question> selectedItems = grid.getSelectedItems();
            Interview interview = new Interview();
            for (Question q : selectedItems) {
                interview.getInterviewQuestions().add(new InterviewQuestion(q));
            }
            candidate.addInterview(interview);
            interview.setCandidate(candidate);
            candidateRepository.save(candidate);
            binder.readBean(null);
            Notification.show("Сохранено", 1000, Notification.Position.MIDDLE);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    private void processSearch(String searchString) {
        if (StringUtils.isBlank(searchString)) {
            fillGrid();
            return;
        }
        List<Question> selected = CategorySearcher.search(searchString, questionRepository.findAll());
        if (selected == null) return;
        grid.setItems(selected);
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
