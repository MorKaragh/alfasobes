package ru.alfasobes.alfasobes.ui;

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
import ru.alfasobes.alfasobes.dao.InterviewRepository;
import ru.alfasobes.alfasobes.model.Interview;
import ru.alfasobes.alfasobes.util.Const;

import javax.annotation.PostConstruct;

@Component
@Route(value = Const.JOURNAL_PAGE, layout = MainView.class)
@UIScope
@StyleSheet(Const.STYLES)
public class Journal extends VerticalLayout {

    @Autowired
    private InterviewRepository interviewRepository;

    private Grid<Interview> grid = new Grid<>();

    @PostConstruct
    public void postConstruct() {

        Iterable<Interview> all = interviewRepository.findAll();
        grid.addColumn(interview -> interview.getCandidate().getName(),"кандидат");
        grid.addColumn(Interview::getFinishDateString,"дата");
        grid.addColumn(interview -> interview.getStatus().getDescription(),"статус");

        grid.addItemClickListener(new ComponentEventListener<ItemClickEvent<Interview>>() {
            @Override
            public void onComponentEvent(ItemClickEvent<Interview> interviewItemClickEvent) {

            }
        });

        add(grid);

    }

}
