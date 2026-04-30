# Data Structure Advisor

Data Structure Advisor is a Java desktop application that helps students compare common Java data structures and choose an appropriate structure for a programming task.

The app provides an interactive GUI with questions, filters, ranked data structure suggestions, visual diagrams, explanations, API summaries, and example Java code.

## Features

- Compare common Java data structures:
  - ArrayList
  - Stack
  - Queue
  - PriorityQueue
  - ArrayDeque
  - HashSet
  - TreeSet
  - HashMap
  - TreeMap
- Filter by:
  - key-value mapping
  - duplicate support
  - sorted order
  - index access
  - removal order
- Rank results by:
  - lookup speed
  - add/delete speed
  - memory efficiency
- View themed diagrams for each structure
- Switch between light, soft blue, dark, and dark blue themes
- Read explanations, API overviews, and example code

## Requirements

- Java 25
- Maven

## Running the App

From the project root:

```bash
mvn clean compile
mvn exec:java