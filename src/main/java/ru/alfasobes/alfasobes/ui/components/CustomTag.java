package ru.alfasobes.alfasobes.ui.components;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class CustomTag extends Div {
    Button closeBtn;

    public CustomTag(String tagText) {
        this.setClassName("post-tag");
        Label label = new Label(tagText);
        label.getStyle().set("margin-top", "5px");
        label.getStyle().set("margin-bottom", "5px");
        label.getStyle().set("margin-left", "10px");

        Icon close = VaadinIcon.CLOSE.create();
        closeBtn = new Button(close);
        closeBtn.getStyle().set("background", "transparent");
        closeBtn.getStyle().set("border-radius", "15px");
        closeBtn.getStyle().set("padding", "-15px");

        add(label);

        add(closeBtn);
    }
}
