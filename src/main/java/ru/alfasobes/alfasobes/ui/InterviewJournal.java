package ru.alfasobes.alfasobes.ui;

import com.google.common.collect.Lists;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.alfasobes.alfasobes.dao.InterviewRepository;
import ru.alfasobes.alfasobes.model.Interview;
import ru.alfasobes.alfasobes.model.InterviewStatus;
import ru.alfasobes.alfasobes.util.Const;

import javax.annotation.PostConstruct;

@Component
@Route(value = Const.JOURNAL_PAGE, layout = MainView.class)
@UIScope
@StyleSheet(Const.STYLES)
public class InterviewJournal extends VerticalLayout {


    @Autowired
    private ApplicationContext context;

    @Autowired
    private InterviewRepository interviewRepository;

    private Grid<Interview> grid = new Grid<>();

    @PostConstruct
    public void postConstruct() {

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        grid.addColumn(interview -> interview.getCandidate().getName())
                .setHeader("Кандидат")
                .setSortable(true);

        grid.addColumn(new LocalDateTimeRenderer<>(Interview::getDate,"dd.MM.yyyy"))
                .setHeader("Дата завершения")
                .setWidth("100px")
                .setSortable(true);

        grid.addColumn(interview -> interview.getStatus().getDescription())
                .setHeader("Статус")
                .setWidth("100px")
                .setSortable(true);

        grid.addColumn(
                new NativeButtonRenderer<>("Завершить", clickedItem -> {
                    clickedItem.setStatus(InterviewStatus.FINISHED);
                    interviewRepository.save(clickedItem);
                })
        );

        grid.addItemDoubleClickListener(new ComponentEventListener<ItemDoubleClickEvent<Interview>>() {
            @Override
            public void onComponentEvent(ItemDoubleClickEvent<Interview> interviewItemDoubleClickEvent) {
                remove(grid);
                InterviewDialog interviewDialog = context.getBean(InterviewDialog.class);
                interviewDialog.setInterview(interviewItemDoubleClickEvent.getItem());
                add(interviewDialog);
            }
        });

        add(grid);

        addAttachListener(new ComponentEventListener<AttachEvent>() {
            @Override
            public void onComponentEvent(AttachEvent attachEvent) {
                fillGrid();
            }
        });

    }

    private void fillGrid() {
        Iterable<Interview> all = interviewRepository.findAll();
        grid.setItems(Lists.newArrayList(all));
    }

}
