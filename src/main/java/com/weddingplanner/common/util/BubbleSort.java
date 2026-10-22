package com.weddingplanner.common.util;

import com.weddingplanner.common.datastructure.CustomLinkedList;

import java.util.Comparator;

/**
 * Bubble sort implemented by hand (no Collections.sort / List.sort).
 * Used to sort vendors by price. Owner: Vendor Management member.
 *
 * Example:
 *   BubbleSort.sort(list, Comparator.comparingDouble(Vendor::getPrice));            // low -> high
 *   BubbleSort.sort(list, Comparator.comparingDouble(Vendor::getPrice).reversed()); // high -> low
 */
public final class BubbleSort {

    private BubbleSort() {
    }

    /** Sorts the linked list in place by swapping node data. */
    public static <T> void sort(CustomLinkedList<T> list, Comparator<? super T> comparator) {
        int n = list.size();
        for (int pass = 0; pass < n - 1; pass++) {
            boolean swapped = false;
            CustomLinkedList.Node<T> cur = list.getHead();
            for (int j = 0; j < n - pass - 1; j++) {
                CustomLinkedList.Node<T> next = cur.getNext();
                if (comparator.compare(cur.getData(), next.getData()) > 0) {
                    T tmp = cur.getData();
                    cur.setData(next.getData());
                    next.setData(tmp);
                    swapped = true;
                }
                cur = next;
            }
            if (!swapped) {
                break;
            }
        }
    }
}
