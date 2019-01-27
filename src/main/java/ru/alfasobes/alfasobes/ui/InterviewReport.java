package ru.alfasobes.alfasobes.ui;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.IconRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.alfasobes.alfasobes.dao.InterviewRepository;
import ru.alfasobes.alfasobes.model.Interview;
import ru.alfasobes.alfasobes.model.InterviewQuestion;
import ru.alfasobes.alfasobes.util.Const;

import java.util.Optional;

@Component
@Scope("prototype")
@Route("report")
public class InterviewReport extends VerticalLayout implements HasUrlParameter<String> {

    @Autowired
    private InterviewRepository repository;

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        if (s == null) {
            UI.getCurrent().navigate(Const.JOURNAL_PAGE);
        } else {
            Optional<Interview> byId = repository.findById(Long.valueOf(s));
            if (byId.isPresent()) {
                showReport(byId.get());
            } else {
                UI.getCurrent().navigate(Const.JOURNAL_PAGE);
            }
        }
    }

    private void showReport(Interview interview) {
        add(new Html("<H1>" + interview.getCandidate().getName() + "</H1>"));
        add(new Html("<div>дата интервью: " + interview.getFinishDateString() + "</div>"));
        Grid<InterviewQuestion> grid = buildGrid(interview);
        add(grid);
    }

    private Grid<InterviewQuestion> buildGrid(Interview interview) {
        Grid<InterviewQuestion> grid = new Grid<>();

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        grid.addColumn(question -> question.getQuestion().getQuestion())
                .setHeader("вопрос")
                .setSortable(true);

        grid.addColumn(question -> question.getAnswer().getDescription())
                .setHeader("ответ")
                .setSortable(true);

        grid.addColumn(new IconRenderer<>(question -> {
            String label = "";
            switch (question.getAnswer()) {
                case BAD:
                    label = "<div style=\"background-color:" + Const.RED + "; color:white; padding: 0 0 0 7px; border-radius: 8px;\">"
                            + question.getQuestion().getCategories() + "</div>";
                    break;
                case GOOD:
                    label = "<div style=\"background-color:" + Const.GREEN + "; color:white; padding: 0 0 0 7px; border-radius: 8px;\">"
                            + question.getQuestion().getCategories() + "</div>";
                    break;
                case MODERATE:
                    label = "<div style=\"background-color:" + Const.BLUE + "; color:white; padding: 0 0 0 7px; border-radius: 8px;\">"
                            + question.getQuestion().getCategories() + "</div>";
                    break;
            }
            return new Html(label);
        }, question -> ""))
                .setHeader("категория");


        grid.setItems(interview.getInterviewQuestions().stream().filter(question -> question.getAnswer() != null));
        return grid;
    }

}
