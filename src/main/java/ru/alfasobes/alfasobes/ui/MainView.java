package ru.alfasobes.alfasobes.ui;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.applayout.AbstractAppRouterLayout;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import ru.alfasobes.alfasobes.util.Const;

import javax.annotation.PostConstruct;

@Component
@UIScope
@Route("")
public class MainView extends AbstractAppRouterLayout {

    @PostConstruct
    public void construct() {

    }

    @Override
    protected void configure(AppLayout appLayout, AppLayoutMenu appLayoutMenu) {
        AppLayoutMenu menu = getAppLayout().createMenu();

        AppLayoutMenuItem prepareInterview = new AppLayoutMenuItem("Подготовить интервью", Const.COMPOSE_INTERVIEW_PAGE);
        AppLayoutMenuItem newQuestion = new AppLayoutMenuItem("Добавить вопрос", Const.NEW_QUESTION_PAGE);
        AppLayoutMenuItem journal = new AppLayoutMenuItem("Журнал интервью", Const.JOURNAL_PAGE);

        menu.addMenuItems(
                journal,
                newQuestion,
                prepareInterview
        );

        getAppLayout().setBranding(new Label("АльфаПодбор"));
    }
}
