package dk.dtu.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CircularList {
    private List<Object> objects;
    private int firstObjectIndex;

    public CircularList() {
        this.objects = new ArrayList<>();
        this.firstObjectIndex = 0;
    }

    private int getObjectsIndex(int index) {
        return  (firstObjectIndex + index) % objects.size();
    }

    public Object getNext() {
        Object next = objects.get(firstObjectIndex);
        firstObjectIndex = getObjectsIndex(1);
        return next;
    }

    public void setObjects(List<Object> objects) {
        this.objects = objects;
    }

}
