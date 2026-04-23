

# DataStructure Decision Tool

A professional-grade Java application designed to assist developers and students in selecting the most efficient Java Collection Framework structure for their specific performance needs.

## Why This Tool?
In Java development, choosing the wrong data structure can lead to significant performance bottlenecks. This tool eliminates the guesswork by analyzing your specific performance requirements—such as speed, memory usage, and data constraints—to recommend the ideal structure for your use case.

## How It Works (End-User Experience)
As an end-user, you simply follow these three steps:

1. **Define Your Needs**: You input your constraints (e.g., "Do I need unique elements?" or "Do I need key-value mapping?") and your performance priorities (e.g., "How important is fast lookup speed?").
2. **Automated Analysis**: The tool's engine processes your requirements against its internal library of Java structures (like `ArrayList`, `HashMap`, `TreeSet`, etc.).
3. **Optimized Recommendation**: The tool returns a ranked list of the best structures for your task, along with an "Expert Note" if you have selected an older, legacy structure that has a better modern alternative.

## Architectural Highlights
This project demonstrates professional OOP principles:
- **Separation of Concerns**: The evaluation logic is completely decoupled from the data structures.
- **Strategy Pattern**: The scoring engine can be swapped or upgraded without modifying the library of structures.
- **Maintainable Design**: Using Maven and standard Java packaging, the tool is built to be easily extended with new data structures.

## Getting Started
1. **Prerequisites**: Ensure you have Java 17 and Maven installed.
2. **Build**: Run `mvn clean install` in the project root to compile the application.
3. **Run**: Start the application and follow the interactive console prompts to receive your data structure recommendation.

---
*Developed as part of the COP-3330 Object-Oriented Programming coursework.*
README.md
Displaying README.md.
