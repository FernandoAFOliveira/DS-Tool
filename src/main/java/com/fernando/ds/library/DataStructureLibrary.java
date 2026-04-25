package com.fernando.ds.library;

import java.util.ArrayList;
import java.util.List;

import com.fernando.ds.model.DataStructure;
import com.fernando.ds.model.DSArrayDeque;
import com.fernando.ds.model.DSArrayList;
import com.fernando.ds.model.DSHashMap;
import com.fernando.ds.model.DSHashSet;
import com.fernando.ds.model.DSPriorityQueue;
import com.fernando.ds.model.DSQueue;
import com.fernando.ds.model.DSStack;
import com.fernando.ds.model.DSTreeMap;
import com.fernando.ds.model.DSTreeSet;

public class DataStructureLibrary {

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