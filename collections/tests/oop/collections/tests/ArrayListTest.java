package oop.collections.tests;

import oop.collections.IList;
import oop.utils.collections.ArrayList;

public class ArrayListTest extends ListTest {
    public ArrayListTest() {
        super("ArrayList", new ArrayList());
    }

    @Override
    protected IList createEmptyList() {
        return new ArrayList();
    }

    @Override
    protected IList createList(Object[] elements) {
        return new ArrayList(elements);
    }
}