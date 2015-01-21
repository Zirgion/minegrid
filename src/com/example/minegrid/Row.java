package com.example.minegrid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.example.minegrid.data.MineGenerator;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;

public class Row implements Item {
    public enum State {
        EMPTY(0), MINE(-1), UNSEEN_MINE(2), UNSEEN_EMPTY(-2), MARKED_EMPTY(1), MARKED_MINE(
                -3);
        public int value;

        private State(int value) {
            this.value = value;
        }
    }

    private List<Row.State> columns;
    private Collection<Integer> itemPropertyIds;
    private MineGenerator mines;

    public Row(MineGenerator mines, Collection<Integer> itemPropertyIds) {
        this.mines = mines;
        this.itemPropertyIds = itemPropertyIds;

        columns = new ArrayList<Row.State>();
        itemPropertyIds = new ArrayList<>();
        for (int i = 0; i < mines.getWidth(); i++) {
            columns.add(State.UNSEEN_EMPTY);
        }
    }

    public void setState(int column, Row.State state) {
        columns.set(column, state);
    }

    @Override
    public Property<String> getItemProperty(Object column) {
        int i = ((Integer) column);
        if (i >= columns.size()) {
            return null;
        }
        String display;
        switch (columns.get(i)) {
        case EMPTY:
            int numMines = mines.minesNextTo(this, i);
            if (numMines > 0) {
                display = numMines + "";
            } else {
                display = "";
            }
            break;
        case MARKED_EMPTY:
        case MARKED_MINE:
            display = FontAwesome.FLAG.getHtml();
            break;
        case MINE:
            display = FontAwesome.BOMB.getHtml();
            break;
        case UNSEEN_EMPTY:
        case UNSEEN_MINE:
            display = FontAwesome.QUESTION.getHtml();
            break;
        default:
            display = "UNKNOWN";
            break;
        }
        return new StateProperty(display);
    }

    @Override
    public Collection<?> getItemPropertyIds() {
        return itemPropertyIds;
    }

    @Override
    public boolean addItemProperty(Object id,
            @SuppressWarnings("rawtypes") Property property)
            throws UnsupportedOperationException {
        return false;
    }

    @Override
    public boolean removeItemProperty(Object id)
            throws UnsupportedOperationException {
        return false;
    }

    public State getState(int column) {
        return columns.get(column);
    }

    public State getOriginalState() {
        // TODO Auto-generated method stub
        return null;
    }
}
