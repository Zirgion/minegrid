package com.example.minegrid;

import javax.servlet.annotation.WebServlet;

import com.example.minegrid.data.MineGenerator;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderer.HtmlRenderer;

@SuppressWarnings("serial")
@Theme("minegrid")
public class MinegridUI extends UI implements ItemClickListener {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MinegridUI.class, widgetset = "com.example.minegrid.widgetset.MinegridWidgetset")
    public static class Servlet extends VaadinServlet {
    }

    private MineGenerator mines;

    private static final int WIDTH = 40;
    private static final int HEIGHT = 1000;
    private static final int MINES = 5000;

    private Label minesLabel;
    private Label scoreLabel;
    private int score;

    @Override
    protected void init(VaadinRequest request) {
        mines = new MineGenerator(WIDTH, HEIGHT, MINES);

        VerticalLayout layout = new VerticalLayout();

        HorizontalLayout labels = new HorizontalLayout();
        Label instructions = new Label(
                "Click to open a square, ctrl-click to mark as mine.");
        instructions.setSizeUndefined();
        labels.addComponent(instructions);

        minesLabel = new Label();
        minesLabel.setSizeUndefined();
        labels.addComponent(minesLabel);

        scoreLabel = new Label();
        scoreLabel.setSizeUndefined();
        labels.addComponent(scoreLabel);

        updateLabels();

        labels.setExpandRatio(instructions, 1);
        labels.setWidth("100%");
        labels.setMargin(true);
        labels.setSpacing(true);
        layout.addComponent(labels);

        Grid field = new Grid();
        layout.addComponent(field);
        field.setSelectionMode(SelectionMode.NONE);
        field.setContainerDataSource(mines);

        field.setHeaderVisible(false);

        field.addItemClickListener(this);

        for (Grid.Column column : field.getColumns()) {
            column.setWidth(41);
            column.setRenderer(new HtmlRenderer());
        }
        field.setHeight("100%");
        // 21 px is for the Valo scroll bar
        field.setWidth(21 + 41 * WIDTH + "px");

        layout.setExpandRatio(field, 1);
        layout.setSizeFull();
        setSizeFull();
        setContent(layout);
    }

    @Override
    public void itemClick(ItemClickEvent event) {
        Integer column = (Integer) event.getPropertyId();
        Row row = (Row) event.getItem();
        if (event.isCtrlKey()) {
            mines.mark(row, column);
        } else {
            score += mines.open(row, column);
        }
        updateLabels();
    }

    private void updateLabels() {
        minesLabel.setValue("Mines: " + mines.getMineCount());
        scoreLabel.setValue("Score: " + score);
    }
}