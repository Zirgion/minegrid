package com.example.minegrid.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.example.minegrid.Row;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractContainer;

@SuppressWarnings("serial")
public class MineGenerator extends AbstractContainer implements
        Container.ItemSetChangeNotifier, Container.Indexed, Container.Sortable {

    private List<Row> dataProvider;
    private int storedSize = -1;

    private Collection<Integer> itemPropertyIds;
    private int width;
    private int height;
    private Collection<Integer> itemIds;

    private int mineCount = 0;

    public MineGenerator(int width, int height, int mines) {
        this.width = width;
        this.height = height;

        mineCount = mines;

        dataProvider = new ArrayList<>(height);

        itemIds = new ArrayList<Integer>();
        for (int i = 0; i < size(); i++) {
            itemIds.add(i);
        }

        itemPropertyIds = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            itemPropertyIds.add(i);
        }

        for (int i = 0; i < height; i++) {
            Row item = new Row(this, itemPropertyIds);
            dataProvider.add(item);
        }

        for (int i = 0; i < mines; i++) {
            int x = (int) (Math.random() * width);
            int y = (int) (Math.random() * height);

            Row.State s = dataProvider.get(y).getState(x);
            if (s == Row.State.UNSEEN_MINE) {
                i--;
                continue;
            }
            dataProvider.get(y).setState(x, Row.State.UNSEEN_MINE);
        }
    }

    public int minesNextTo(Row row, int column) {
        int mines = horizontalMinesNextTo(row, column)
                + horizontalMinesNextTo(getItem(dataProvider.indexOf(row) - 1),
                        column)
                + horizontalMinesNextTo(getItem(dataProvider.indexOf(row) + 1),
                        column);

        return mines;
    }

    private int horizontalMinesNextTo(Row row, int column) {
        if (row == null) {
            return 0;
        }
        int mines = 0;
        if (row.getState(column).value > 0) {
            mines++;
        }
        if (column > 0 && row.getState(column - 1).value > 0) {
            mines++;
        }
        if (column < width - 1 && row.getState(column + 1).value > 0) {
            mines++;
        }

        return mines;
    }

    /****************************************/
    /** Methods implemented from Container **/
    /****************************************/
    @Override
    public Row getItem(Object itemId) {
        if (containsId(itemId)) {
            int index = (Integer) itemId;
            return dataProvider.get(index);
        }
        return null;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Property getContainerProperty(Object itemId, Object propertyId) {
        Item item = getItem(itemId);
        return item != null ? item.getItemProperty(propertyId) : null;
    }

    @Override
    public boolean containsId(Object itemId) {
        return itemId instanceof Integer && (Integer) itemId >= 0
                && (Integer) itemId < size();
    }

    @Override
    public Collection<Integer> getItemIds() {
        return itemIds;
    }

    @Override
    public int size() {
        int newSize = getCount();
        if (storedSize != newSize) {
            storedSize = newSize;
            fireItemSetChange();
        }
        return storedSize;
    }

    @Override
    public Collection<?> getContainerPropertyIds() {
        return itemPropertyIds;
    }

    @Override
    public Class<?> getType(Object propertyId) {
        return String.class;
    }

    /**************************************************************/
    /** Methods implemented from Container.ItemSetChangeNotifier **/
    /**************************************************************/

    @Override
    public void addItemSetChangeListener(ItemSetChangeListener listener) {
        super.addItemSetChangeListener(listener);
    }

    @Override
    public void addListener(ItemSetChangeListener listener) {
        super.addItemSetChangeListener(listener);
    }

    @Override
    public void removeItemSetChangeListener(ItemSetChangeListener listener) {
        super.removeItemSetChangeListener(listener);
    }

    @Override
    public void removeListener(ItemSetChangeListener listener) {
        super.removeItemSetChangeListener(listener);
    }

    /************************************************/
    /** Methods implemented from Container.Indexed **/
    /************************************************/
    @Override
    public int indexOfId(Object itemId) {
        return containsId(itemId) ? (Integer) itemId : -1;
    }

    @Override
    public Object getIdByIndex(int index) {
        if (containsId(index)) {
            return new Integer(index);
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public List<?> getItemIds(int startIndex, int numberOfItems) {
        if (numberOfItems < 0) {
            throw new IllegalArgumentException();
        }
        if (!containsId(startIndex)) {
            throw new IndexOutOfBoundsException();
        }
        int endIndex = startIndex + numberOfItems - 1;
        if (endIndex > size() - 1) {
            endIndex = size() - 1;
        }
        List<Integer> itemIds = new ArrayList<Integer>();
        for (int i = startIndex; i <= endIndex; i++) {
            itemIds.add(i);
        }
        return itemIds;
    }

    /************************************************/
    /** Methods implemented from Container.Ordered **/
    /************************************************/
    @Override
    public Object nextItemId(Object itemId) {
        return containsId(itemId) && (Integer) itemId < size() - 1 ? (Integer) itemId + 1
                : null;
    }

    @Override
    public Object prevItemId(Object itemId) {
        return containsId(itemId) && (Integer) itemId > 0 ? (Integer) itemId - 1
                : null;
    }

    @Override
    public Object firstItemId() {
        return size() > 0 ? 0 : null;
    }

    @Override
    public Object lastItemId() {
        return size() > 0 ? size() - 1 : null;
    }

    @Override
    public boolean isFirstId(Object itemId) {
        return containsId(itemId) && (Integer) itemId == 0;
    }

    @Override
    public boolean isLastId(Object itemId) {
        return containsId(itemId) && (Integer) itemId == size() - 1;
    }

    /****************************************************/
    /** Not to be implemented in a read-only container **/
    /****************************************************/
    @Override
    public Item addItem(Object itemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object addItem() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeItem(Object itemId)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addContainerProperty(Object propertyId, Class<?> type,
            Object defaultValue) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeContainerProperty(Object propertyId)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object addItemAt(int index) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item addItemAt(int index, Object newItemId)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object addItemAfter(Object previousItemId)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item addItemAfter(Object previousItemId, Object newItemId)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    protected int getCount() {
        return dataProvider.size();
    }

    // The Container.Sortable has to be implemented to avoid
    // http://dev.vaadin.com/ticket/16311
    @Override
    public void sort(Object[] propertyId, boolean[] ascending) {
        // do nothing
    }

    @Override
    public Collection<?> getSortableContainerPropertyIds() {
        return new ArrayList<>();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int open(Row row, Integer column) {
        return open(row, column, true);
    }

    private int open(Row row, Integer column, boolean sendChangeEvent) {
        if (row == null || column < 0 || column >= width) {
            return 0;
        }

        switch (row.getState(column)) {
        case EMPTY:
            break;
        case MARKED_EMPTY:
        case MARKED_MINE:
            break;
        case MINE:
            break;
        case UNSEEN_EMPTY:
            int score = 1;
            row.setState(column, Row.State.EMPTY);
            if (minesNextTo(row, column) == 0) {
                score += open(row, column - 1, false);
                score += open(row, column + 1, false);
                Row previous = getItem(dataProvider.indexOf(row) - 1);
                score += open(previous, column - 1, false);
                score += open(previous, column, false);
                score += open(previous, column + 1, false);
                Row next = getItem(dataProvider.indexOf(row) + 1);
                score += open(next, column - 1, false);
                score += open(next, column, false);
                score += open(next, column + 1, false);
            }
            if (sendChangeEvent) {
                super.fireItemSetChange();
            }
            return score;
        case UNSEEN_MINE:
            mineCount--;
            row.setState(column, Row.State.MINE);
            super.fireItemSetChange();
            return -5;
        }
        return 0;
    }

    public void mark(Row row, Integer column) {
        switch (row.getState(column)) {
        case MARKED_EMPTY:
            mineCount++;
            row.setState(column, Row.State.UNSEEN_EMPTY);
            super.fireItemSetChange();
            break;
        case MARKED_MINE:
            mineCount++;
            row.setState(column, Row.State.UNSEEN_MINE);
            super.fireItemSetChange();
            break;
        case UNSEEN_EMPTY:
            mineCount--;
            row.setState(column, Row.State.MARKED_EMPTY);
            super.fireItemSetChange();
            break;
        case UNSEEN_MINE:
            mineCount--;
            row.setState(column, Row.State.MARKED_MINE);
            super.fireItemSetChange();
            break;
        }
    }

    public int getMineCount() {
        return mineCount;
    }
}
