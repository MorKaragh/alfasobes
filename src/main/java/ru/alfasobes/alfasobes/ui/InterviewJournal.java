package ru.alfasobes.alfasobes.ui;

import com.google.common.collect.Lists;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.alfasobes.alfasobes.dao.InterviewRepository;
import ru.alfasobes.alfasobes.model.Interview;
import ru.alfasobes.alfasobes.model.InterviewStatus;
import ru.alfasobes.alfasobes.ui.common.ConfirmDialog;
import ru.alfasobes.alfasobes.util.Const;

import javax.annotation.PostConstruct;

@Component
@Route(value = Const.JOURNAL_PAGE, layout = MainView.class)
@RouteAlias(value = "")
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
        createGrid();
        addAttachListener((ComponentEventListener<AttachEvent>) attachEvent -> fillGrid());
    }

    private void createGrid() {
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        grid.addColumn(interview -> interview.getCandidate().getName())
                .setHeader("Кандидат")
                .setSortable(true);

        grid.addColumn(new LocalDateTimeRenderer<>(Interview::getDate, "dd.MM.yyyy"))
                .setHeader("Дата завершения")
                .setWidth("100px")
                .setSortable(true)
                .setComparator((interview, t1) -> {
                    if (interview.getDate() == null) return 1;
                    if (t1.getDate() == null) return -1;
                    return interview.getDate().compareTo(t1.getDate());
                });

        grid.addColumn(interview -> interview.getStatus().getDescription())
                .setHeader("Статус")
                .setWidth("100px")
                .setSortable(true);

        grid.addColumn(
                new NativeButtonRenderer<>(interview -> {
                    if (InterviewStatus.CREATED.equals(interview.getStatus())) {
                        return ("начать");
                    } else if (InterviewStatus.UNFINISHED.equals(interview.getStatus())) {
                        return ("открыть");
                    } else if (InterviewStatus.FINISHED.equals(interview.getStatus())) {
                        return ("отчет");
                    }
                    return "";
                }, this::action)
        );


        grid.addItemDoubleClickListener((ComponentEventListener<ItemDoubleClickEvent<Interview>>)
                interviewItemDoubleClickEvent -> {
                    Interview item = interviewItemDoubleClickEvent.getItem();
                    action(item);
                });

        add(grid);
    }

    private void action(Interview interview) {
        if (InterviewStatus.CREATED.equals(interview.getStatus())
                || InterviewStatus.UNFINISHED.equals(interview.getStatus())) {
            start(interview);
        } else if (InterviewStatus.FINISHED.equals(interview.getStatus())) {
            operReport(interview);
        }
    }

    private void operReport(Interview interview) {
        UI.getCurrent().navigate("report/" + interview.getId());
    }

    private void start(Interview item) {
        remove(grid);
        InterviewDialog interviewDialog = context.getBean(InterviewDialog.class);
        interviewDialog.setInterview(item);
        interviewDialog.addFinishListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> finish(item));
        add(interviewDialog);
    }

    private void finish(Interview clickedItem) {
        ConfirmDialog confirm = new ConfirmDialog("Завершить интервью?", buttonClickEvent -> {
            clickedItem.setStatus(InterviewStatus.FINISHED);
            interviewRepository.save(clickedItem);
            UI.getCurrent().getPage().reload();
        });
        confirm.open();
    }

    private void fillGrid() {
        Iterable<Interview> all = interviewRepository.findAll();
        grid.setItems(Lists.newArrayList(all));
    }

}
