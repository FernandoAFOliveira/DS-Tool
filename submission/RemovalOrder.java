/*
 * Fernando Fonteles Oliveira
 * UCF ID: 5676172
 * 2026/Apr/26
 * COP 3330 Object-Oriented Programming
 * Professor: Arup Guha
 * Programming Assignment 10 Free Choice Project * 
 * Data Structure Advisor Submission *
 * File: RemovalOrder.java
 *
 * RemovalOrder represents how a structure removes elements.
 * This enum makes FIFO, LIFO, priority, and double-ended behavior explicit.
 */

public enum RemovalOrder {
    ANY,
    FIFO,
    LIFO,
    DOUBLE_ENDED,
    PRIORITY
}