package ru.alfasobes.alfasobes.ui;

import com.google.common.collect.Lists;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.alfasobes.alfasobes.dao.CandidateRepository;
import ru.alfasobes.alfasobes.dao.InterviewRepository;
import ru.alfasobes.alfasobes.model.Candidate;
import ru.alfasobes.alfasobes.model.Interview;
import ru.alfasobes.alfasobes.util.Const;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Route(value = Const.JOURNAL_PAGE, layout = MainView.class)
@UIScope
@StyleSheet(Const.STYLES)
public class Journal extends VerticalLayout {

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    private Grid<Interview> grid = new Grid<>();

    @PostConstruct
    public void postConstruct() {

        Iterable<Interview> all = interviewRepository.findAll();
        Iterable<Candidate> candidates = candidateRepository.findAll();
        grid.addColumn(interview -> interview.getCandidate().getName(),"кандидат");
        grid.addColumn(Interview::getFinishDateString,"дата");
        grid.addColumn(interview -> interview.getStatus().getDescription(),"статус");

        grid.setItems(Lists.newArrayList(all));

        grid.addItemClickListener(new ComponentEventListener<ItemClickEvent<Interview>>() {
            @Override
            public void onComponentEvent(ItemClickEvent<Interview> interviewItemClickEvent) {

            }
        });

        add(grid);

    }

}
