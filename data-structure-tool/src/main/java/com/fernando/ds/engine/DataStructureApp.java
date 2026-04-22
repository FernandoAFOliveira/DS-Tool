package com.fernando.ds.engine;

import java.util.ArrayList;
import java.util.List;

import com.fernando.ds.model.*;

public class DataStructureApp {
    public static void main(String[] args) {
        // 1. Initialize the library of available structures
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

        // 2. Capture user requirements
        UserRequirements userReq = collectUserInputs(); 

        // 3. Score and Rank
        // Your logic here to sort the library based on the userReq
    }
}