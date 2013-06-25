package fr.automated.trading.systems.utils.utils;

public class CircularList<Type> {

    public int LIMIT_MAX = 100;
    public int LIMIT_MIN = 0;

    private Type[] list;
    private int count;
    private boolean ascending;
    private boolean firstAdd;

    public CircularList() {
        clear();
    }

    public void add(Type element) {

        if(isFirstAdd()) {
            setFirstAdd(false);
            list[0] = element;
            list[1] = element;
            count++;
            return;
        }

        if(count == LIMIT_MAX)
            setAscending(false);
        if(count == LIMIT_MIN)
            setAscending(true);

        if(isAscending())
            count++;
        else
            count--;

        list[count] = element;
    }

    public Type get(int index) {
        return list[index];
    }

    public Type getPrevious(int index) {
        if(isAscending())
            return list[index-1];
        else
            return list[index+1];
    }

    public Type getLastPrevious() {
        if(isAscending())
            return list[count-1];
        else
            return list[count+1];
    }

    public void clear() {
        LIMIT_MAX = 100;
        LIMIT_MIN = 0;
        count = LIMIT_MIN;
        list = (Type[]) new Object[LIMIT_MAX+1];
        setAscending(true);
        setFirstAdd(true);
    }

    public Type getLast() {
        return list[count];
    }

    private boolean isAscending() {
        return ascending;
    }

    private void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    private boolean isFirstAdd() {
        return firstAdd;
    }

    private void setFirstAdd(boolean firstAdd) {
        this.firstAdd = firstAdd;
    }
}
