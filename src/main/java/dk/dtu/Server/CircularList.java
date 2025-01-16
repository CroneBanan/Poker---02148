package dk.dtu.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CircularList<T> {
    private List<T> objects;
    private int firstObjectIndex;

    public CircularList() {
        this.objects = new ArrayList<>();
        this.firstObjectIndex = 0;
    }

    private int getObjectsIndex(int index) {
        return  (firstObjectIndex + index) % objects.size();
    }

    public T getNext() {
        T next = objects.get(firstObjectIndex);
        firstObjectIndex = getObjectsIndex(1);
        return next;
    }

    public T get(int index) {
        return objects.get(getObjectsIndex(index));
    }

    public void setObjects(List<T> objects) {
        this.objects = objects;
    }

    public List<T> toList() {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            list.add(i, get(i));
        }
        return list;
    }

    public int size() {
        return objects.size();
    }
}
