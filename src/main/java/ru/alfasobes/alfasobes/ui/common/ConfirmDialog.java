package ru.alfasobes.alfasobes.ui.common;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


public class ConfirmDialog extends Dialog {

    public ConfirmDialog(String question,
                         ComponentEventListener<ClickEvent<Button>> onYes){
        Button yes = new Button("Да");
        Button no = new Button("Нет");

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.add(no, yes);

        VerticalLayout content = new VerticalLayout();
        content.add(new Html("<h4>"+question+"</h4>"));
        content.add(buttons);

        buttons.setSizeFull();

        yes.addThemeVariants(ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        no.addThemeVariants(ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

        yes.addClickListener(onYes);

        no.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> close());

        add(content);
    }
}
