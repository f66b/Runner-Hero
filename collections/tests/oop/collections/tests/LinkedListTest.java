package oop.collections.tests;

import oop.collections.IList;
import oop.utils.collections.LinkedList;

public class LinkedListTest extends ListTest {
    public LinkedListTest() {
        super("LinkedList", new LinkedList());
    }

    @Override
    protected IList createEmptyList() {
        return new LinkedList();
    }

    @Override
    protected IList createList(Object[] elements) {
        return new LinkedList(elements);
    }
}