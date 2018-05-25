package at.ac.tuwien.inso.sepm.ticketline.rest.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DuplicateFinder<T> {
    private final Set<T> trashcan = new HashSet<>();
    public final Set<T> Duplicates = new HashSet<>();

    public DuplicateFinder(Collection<T> source) {
        if (source == null) {
            return;
        }

        for (T item : source) {
            if (!trashcan.add(item)) {
                Duplicates.add(item);
            }
        }
    }
}
