package com.fernando.ds.library;

public class QuestionLibrary {

    public static final QuestionInfo[] QUESTIONS = {
        new QuestionInfo(
            "Need key-value mapping?",
            "Choose Yes if each item should be stored with a matching value, like a word with its count, " +
            "a student ID with a student object, or a username with a password hash. In Java, this usually means " +
            "using a Map such as HashMap or TreeMap.\n\n" +
            "Choose No if you only need to store individual values, like a list of names, a set of unique words, " +
            "or a line of tasks.\n\n" +
            "Important idea: Maps store pairs in the form key -> value. Keys must be unique, but different keys may point to the same value.",
            QuestionInfo.QuestionType.YES_NO,
            QuestionInfo.QuestionId.KEY_VALUE
        ),

        new QuestionInfo(
            "Allow duplicates?",
            "Choose Yes if the same value is allowed to appear more than once. For example, an ArrayList can store " +
            "\"apple\", \"apple\", \"banana\".\n\n" +
            "Choose No if each value should appear only once. This usually means using a Set, such as HashSet or TreeSet.\n\n" +
            "If key-value mapping is selected, this question means duplicate values, not duplicate keys. Java maps do not allow duplicate keys. " +
            "If you put the same key again, the old value is replaced.",
            QuestionInfo.QuestionType.YES_NO,
            QuestionInfo.QuestionId.DUPLICATES
        ),

        new QuestionInfo(
            "Need sorted order?",
            "Choose Yes if the structure should keep items in sorted order or support ordered operations. " +
            "For example, you may want the smallest value, largest value, next higher value, or next lower value.\n\n" +
            "In Java, TreeSet and TreeMap are good examples. They are usually slower than HashSet and HashMap for basic lookup, " +
            "but they keep data ordered. Many TreeSet and TreeMap operations are O(log n).\n\n" +
            "Choose No if order does not matter and speed is more important. HashSet and HashMap are usually faster for simple lookup, " +
            "often close to O(1) average time.",
            QuestionInfo.QuestionType.YES_NO,
            QuestionInfo.QuestionId.SORTED
        ),

        new QuestionInfo(
            "Need index access?",
            "Choose Yes if you need to access items by position, such as list.get(0), list.get(5), or list.set(2, value). " +
            "This is useful when position matters or when you need fast random access.\n\n" +
            "In Java, ArrayList is the main structure here. Getting an item by index in an ArrayList is O(1).\n\n" +
            "Choose No if you do not care about positions and only need behavior like lookup, uniqueness, queue order, stack order, or key-value mapping.",
            QuestionInfo.QuestionType.YES_NO,
            QuestionInfo.QuestionId.INDEXED
        ),

        new QuestionInfo(
            "Removal order?",
            "Choose how items should be removed from the structure.\n\n" +
            "Any: Removal order does not matter.\n\n" +
            "FIFO: First In, First Out. The first item added is the first item removed. This is like a line of people waiting. " +
            "Use this for Queue behavior.\n\n" +
            "LIFO: Last In, First Out. The most recent item added is the first item removed. This is like a stack of plates. " +
            "Use this for Stack behavior.\n\n" +
            "DE: Double Ended. Items can be added or removed efficiently from both the front and the back. " +
            "Use this for ArrayDeque behavior.\n\n" +
            "PRI: Priority. The next item removed is based on priority, not insertion order. In Java PriorityQueue, " +
            "the smallest item usually comes out first unless you provide a custom Comparator.",
            QuestionInfo.QuestionType.REMOVAL_ORDER,
            QuestionInfo.QuestionId.REMOVAL_ORDER
        ),

        new QuestionInfo(
            "Lookup speed",
            "This controls how much the app should care about quickly finding or checking an item.\n\n" +
            "High lookup priority favors structures like HashMap and HashSet, which usually have O(1) average lookup time.\n\n" +
            "TreeMap and TreeSet usually have O(log n) lookup time, but they keep items sorted. ArrayList lookup by index is O(1), " +
            "but searching for a value with contains() may take O(n).\n\n" +
            "Set this high when your program often asks questions like: Does this value exist? What value belongs to this key? " +
            "Have I seen this item before?",
            QuestionInfo.QuestionType.SCALE,
            QuestionInfo.QuestionId.LOOKUP
        ),

        new QuestionInfo(
            "Add/Delete speed",
            "This controls how much the app should care about fast insertion and removal.\n\n" +
            "HashMap and HashSet usually add and remove in O(1) average time. TreeMap and TreeSet usually add and remove in O(log n) time. " +
            "ArrayDeque is very good for adding and removing at the front or back.\n\n" +
            "ArrayList is fast when adding to the end, usually O(1) amortized, but inserting or removing from the middle is O(n) " +
            "because later items must shift.\n\n" +
            "Set this high when your program frequently adds, removes, pushes, pops, enqueues, or dequeues items.",
            QuestionInfo.QuestionType.SCALE,
            QuestionInfo.QuestionId.ADD_DELETE
        ),

        new QuestionInfo(
            "Memory efficiency",
            "This controls how much the app should care about using less memory.\n\n" +
            "Some structures use extra memory to get better speed or extra features. HashMap and HashSet use hashing tables. " +
            "TreeMap and TreeSet use tree nodes. Queues, stacks, and deques may also use internal storage depending on the implementation.\n\n" +
            "ArrayList is often memory-friendly because it stores items in a resizable array, although it may keep some extra unused capacity.\n\n" +
            "Set this high when your program stores many items and memory usage matters more than extra features like sorting or key-value lookup.",
            QuestionInfo.QuestionType.SCALE,
            QuestionInfo.QuestionId.MEMORY
        )
    };

    public static QuestionInfo[] getAll() {
        return QUESTIONS;
    }

    private QuestionLibrary() {
        // Prevent object creation.
    }
}