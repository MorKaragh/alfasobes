package ru.alfasobes.alfasobes.ui;

import com.google.common.collect.Lists;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.grid.SortOrderProvider;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;
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
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.stream.Stream;

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

        grid.addColumn(new LocalDateTimeRenderer<>(Interview::getDate, "dd.MM.yyyy"))
                .setHeader("Дата завершения")
                .setWidth("100px")
                .setSortable(true)
                .setComparator(new Comparator<Interview>() {
                    @Override
                    public int compare(Interview interview, Interview t1) {
                        if (interview.getDate() == null) return 1;
                        if (t1.getDate() == null) return -1;
                        return interview.getDate().compareTo(t1.getDate());
                    }
                });

        grid.addColumn(interview -> interview.getStatus().getDescription())
                .setHeader("Статус")
                .setWidth("100px")
                .setSortable(true);

        //grid.addColumn(buildRenderer(Interview::getStatus));

        grid.addColumn(
                new NativeButtonRenderer<>(interview -> {
                    if (InterviewStatus.CREATED.equals(interview.getStatus())) {
                        return("начать");
                    } else if (InterviewStatus.UNFINISHED.equals(interview.getStatus())) {
                        return ("открыть");
                    } else if (InterviewStatus.FINISHED.equals(interview.getStatus())) {
                        return("отчет");
                    }
                    return "";
                }, interview -> {
                    action(interview);
                })
        );


        grid.addItemDoubleClickListener((ComponentEventListener<ItemDoubleClickEvent<Interview>>)
                interviewItemDoubleClickEvent -> {
                    Interview item = interviewItemDoubleClickEvent.getItem();
                    action(item);
                });

        add(grid);

        addAttachListener((ComponentEventListener<AttachEvent>) attachEvent -> fillGrid());

    }

    private void action(Interview interview) {
        if (InterviewStatus.CREATED.equals(interview.getStatus())) {
            start(interview);
        } else if (InterviewStatus.UNFINISHED.equals(interview.getStatus())) {
            start(interview);
        } else if (InterviewStatus.FINISHED.equals(interview.getStatus())) {
            report(interview);
        }
    }

    private void report(Interview interview) {
        UI.getCurrent().navigate("report/"+interview.getId());
    }

    private void start(Interview item) {
        remove(grid);
        InterviewDialog interviewDialog = context.getBean(InterviewDialog.class);
        interviewDialog.setInterview(item);
        interviewDialog.addFinishListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                finish(item);
            }
        });
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
