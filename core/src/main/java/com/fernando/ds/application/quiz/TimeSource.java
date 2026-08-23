package com.fernando.ds.application.quiz;

/**
 * Supplies monotonic time for quiz elapsed-time calculations.
 */
@FunctionalInterface
public interface TimeSource {

    /** @return a monotonic time value in nanoseconds */
    long nanoTime();

    /** @return the system monotonic clock */
    static TimeSource system() {
        return System::nanoTime;
    }
}
