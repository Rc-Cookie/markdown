package com.github.rccookie.markdown;

import java.util.LinkedList;
import java.util.List;

import com.github.rccookie.json.JsonArray;
import com.github.rccookie.json.JsonObject;
import com.github.rccookie.util.Utils;

public abstract class ListElement<T extends Element<?>> implements Element<T> {

    final List<T> children = new LinkedList<>();
    final List<T> view = Utils.view(children);

    @SafeVarargs
    public ListElement(T... children) {
        for(T child : children)
            add(child);
    }

    @Override
    public List<T> children() {
        return view;
    }

    @Override
    public abstract String toString();

    public void add(T child) {
        children.remove(child);
        children.add(child);
    }

    public void add(int index, T child) {
        children.remove(child);
        children.add(index, child);
    }

    public void remove(Object child) {
        //noinspection SuspiciousMethodCalls
        children.remove(child);
    }

    public void remove(int index) {
        children.remove(index);
    }

    public boolean contains(Object child) {
        //noinspection SuspiciousMethodCalls
        return children.contains(child);
    }

    public int indexOf(Object child) {
        //noinspection SuspiciousMethodCalls
        return children.indexOf(child);
    }

    public int childCount() {
        return children.size();
    }


    @Override
    public Object toJson() {
        JsonArray json = new JsonArray();
        for(T child : children)
            json.add(new JsonObject(child.getClass().getSimpleName(), child));
        return json;
    }
}
