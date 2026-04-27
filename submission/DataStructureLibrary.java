import java.util.ArrayList;
import java.util.List;

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