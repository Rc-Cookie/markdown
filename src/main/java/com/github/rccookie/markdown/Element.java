package com.github.rccookie.markdown;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import com.github.rccookie.json.JsonSerializable;

import org.jetbrains.annotations.NotNull;

public interface Element<T extends Element<?>> extends Iterable<T>, JsonSerializable {

    List<T> children();

    @NotNull
    @Override
    default Iterator<T> iterator() {
        return children().iterator();
    }

    @Override
    String toString();

    default Stream<T> stream() {
        return children().stream();
    }
}
