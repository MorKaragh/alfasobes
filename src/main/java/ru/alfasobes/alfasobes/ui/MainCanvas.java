package ru.alfasobes.alfasobes.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import ru.alfasobes.alfasobes.ui.components.CategoryFieldPresenter;
import ru.alfasobes.alfasobes.util.Const;

import java.util.List;

@Route(value = Const.CANVAS, layout = MainView.class)
@StyleSheet("styles.css")
public class MainCanvas extends HorizontalLayout {
    CategoryFieldPresenter categoryFieldPresenter = new CategoryFieldPresenter();
    Button save = new Button("СОХРАНИТЬ");

    public MainCanvas() {
       add(categoryFieldPresenter.buildLayout());
        save.addClickListener(buttonClickEvent -> {
            List<String> data = categoryFieldPresenter.gatherData();
            StringBuilder mergedData = new StringBuilder();
            data.forEach(mergedData::append);
            Notification.show(mergedData.toString());
        });
    }

}
