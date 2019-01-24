package ru.alfasobes.alfasobes.ui;

import com.vaadin.flow.component.applayout.AbstractAppRouterLayout;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.alfasobes.alfasobes.util.Const;

import javax.annotation.PostConstruct;

@Component
@UIScope
public class View extends AbstractAppRouterLayout {

    @PostConstruct
    public void construct() {

    }

    @Override
    protected void configure(AppLayout appLayout, AppLayoutMenu appLayoutMenu) {
        AppLayoutMenu menu = getAppLayout().createMenu();

        menu.addMenuItems(
                new AppLayoutMenuItem("Журнал интервью", Const.APPLY_INTERVIEW_PAGE),
                new AppLayoutMenuItem("Добавить вопрос", Const.NEW_QUESTION_PAGE),
                new AppLayoutMenuItem("Подготовить интервью", Const.COMPOSE_INTERVIEW_PAGE)
        );

        getAppLayout().setBranding(new Label("СОБЕСЕДОВАНИЕ"));
    }
}
