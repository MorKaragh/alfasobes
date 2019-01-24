package ru.alfasobes.alfasobes.ui;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.alfasobes.alfasobes.dao.QuestionRepository;
import ru.alfasobes.alfasobes.model.Question;
import ru.alfasobes.alfasobes.util.Const;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@Route(value = Const.COMPOSE_INTERVIEW_PAGE, layout = View.class)
@UIScope
public class NewInterviewView extends VerticalLayout {

    private TextField name = new TextField("ФИО кандидата");
    private Grid<Question> questionGrid = new Grid<>();
    private Button save = new Button("СОХРАНИТЬ");

    @Autowired
    private QuestionRepository questionRepository;


    @PostConstruct
    public void postConstruct(){

        initGrid();
        fillGrid();

        save.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            questionGrid.getSelectedItems();
        });

        add(name, questionGrid, save);

    }

    private void initGrid() {
        questionGrid.addColumn(Question::getQuestion,"вопрос");
        questionGrid.addColumn(Question::getCategories,"категории");
        questionGrid.setSelectionMode(Grid.SelectionMode.MULTI);
    }

    private void fillGrid() {
        Iterable<Question> all = questionRepository.findAll();
        List<Question> questions = new ArrayList<>();
        all.forEach(questions::add);
        questionGrid.setItems(questions);
    }

}
