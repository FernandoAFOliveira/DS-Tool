/*
 * Fernando Fonteles Oliveira
 * UCF ID: 5676172
 * 2026/Apr/26
 * COP 3330 Object-Oriented Programming
 * Professor: Arup Guha
 * Programming Assignment 10 Free Choice Project * 
 * Data Structure Advisor Submission *
 * File: DataStructureLibrary.java
 *
 * DataStructureLibrary creates the list of available data structures.
 * Keeping the list in one place makes it easier to add or remove structures.
 */

import java.util.ArrayList;
import java.util.List;

public class DataStructureLibrary {

    // This method is the single source of truth for the structures
    // included in the advisor.
    public static List<DataStructure> getAll() {
        List<DataStructure> library = new ArrayList<>();

        library.add(new DSArrayList());
        library.add(new DSStack());
        library.add(new DSQueue());
        library.add(new DSPriorityQueue());
        library.add(new DSArrayDeque());
        library.add(new DSHashSet());
        library.add(new DSTreeSet());
        library.add(new DSHashMap());
        library.add(new DSTreeMap());

        return library;
    }

    private DataStructureLibrary() {
        // Prevent object creation.
    }
}