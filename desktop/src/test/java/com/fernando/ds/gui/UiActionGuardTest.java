package com.fernando.ds.gui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

class UiActionGuardTest {

    @Test
    void runsSuccessfulActionWithoutDisplayingAnError() {
        AtomicBoolean actionRan = new AtomicBoolean();
        RecordingPresenter presenter = new RecordingPresenter();

        UiActionGuard.run("Working feature", () -> actionRan.set(true), presenter);

        assertTrue(actionRan.get());
        assertNull(presenter.errorTitle);
        assertNull(presenter.errorMessage);
    }

    @Test
    void containsRuntimeExceptionLogsItAndDisplaysOnlyGenericError() {
        RuntimeException failure =
            new RuntimeException("database password and internal stack details");
        RecordingPresenter presenter = new RecordingPresenter();
        List<LogRecord> records = captureGuardLogs(() ->
            assertDoesNotThrow(() ->
                UiActionGuard.run("Broken feature", () -> {
                    throw failure;
                }, presenter)
            )
        );

        assertEquals("Broken feature", presenter.errorTitle);
        assertEquals(UiActionGuard.FEATURE_ERROR_MESSAGE, presenter.errorMessage);
        assertFalse(presenter.errorMessage.contains(failure.getMessage()));
        assertTrue(records.stream().anyMatch(record ->
            record.getMessage().contains("Broken feature")
                && record.getThrown() == failure
        ));
    }

    @Test
    void advisorRemainsUsableAfterAnExplorerFailure() {
        RecordingPresenter presenter = new RecordingPresenter();
        AtomicBoolean secondActionRan = new AtomicBoolean();

        captureGuardLogs(() ->
            UiActionGuard.run("Explorer", () -> {
                throw new IllegalStateException("Explorer action failed");
            }, presenter)
        );
        UiActionGuard.run(
            "Advisor",
            () -> secondActionRan.set(true),
            presenter
        );

        assertTrue(secondActionRan.get());
        assertEquals(1, presenter.errorCount);
    }

    @Test
    void advisorRemainsUsableAfterAFlashCardsFailure() {
        RecordingPresenter presenter = new RecordingPresenter();
        AtomicBoolean advisorActionRan = new AtomicBoolean();

        captureGuardLogs(() ->
            UiActionGuard.run("Flash Cards", () -> {
                throw new IllegalStateException("card render failed");
            }, presenter)
        );
        UiActionGuard.run(
            "Advisor",
            () -> advisorActionRan.set(true),
            presenter
        );

        assertTrue(advisorActionRan.get());
        assertEquals("Flash Cards", presenter.errorTitle);
        assertEquals(
            UiActionGuard.FEATURE_ERROR_MESSAGE,
            presenter.errorMessage
        );
        assertEquals(1, presenter.errorCount);
    }

    @Test
    void containsNotificationFailureAndLogsIt() {
        RuntimeException notificationFailure =
            new RuntimeException("dialog subsystem failed");
        UiMessagePresenter failingPresenter = new UiMessagePresenter() {
            @Override
            public void showInformation(String title, String message) {
                throw notificationFailure;
            }

            @Override
            public void showError(String title, String message) {
                throw notificationFailure;
            }
        };

        List<LogRecord> records = captureGuardLogs(() ->
            assertDoesNotThrow(() ->
                UiActionGuard.run("Broken feature", () -> {
                    throw new IllegalStateException("feature failed");
                }, failingPresenter)
            )
        );

        assertTrue(records.stream().anyMatch(record ->
            record.getMessage().contains("Unable to show UI notification")
                && record.getThrown() == notificationFailure
        ));
    }

    @Test
    void reportsUnavailableFeatureWithExactStandardMessage() {
        RecordingPresenter presenter = new RecordingPresenter();

        UiActionGuard.showNotEnabled("Future feature", presenter);

        assertEquals("Future feature", presenter.informationTitle);
        assertEquals(
            "This feature is not enabled yet.",
            presenter.informationMessage
        );
    }

    private static List<LogRecord> captureGuardLogs(Runnable action) {
        Logger logger = Logger.getLogger(UiActionGuard.class.getName());
        List<LogRecord> records = new ArrayList<>();
        Handler handler = new Handler() {
            @Override
            public void publish(LogRecord record) {
                records.add(record);
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() {
            }
        };

        boolean usedParentHandlers = logger.getUseParentHandlers();
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
        try {
            action.run();
        } finally {
            logger.removeHandler(handler);
            logger.setUseParentHandlers(usedParentHandlers);
        }
        return records;
    }

    private static final class RecordingPresenter implements UiMessagePresenter {

        private String informationTitle;
        private String informationMessage;
        private String errorTitle;
        private String errorMessage;
        private int errorCount;

        @Override
        public void showInformation(String title, String message) {
            informationTitle = title;
            informationMessage = message;
        }

        @Override
        public void showError(String title, String message) {
            errorTitle = title;
            errorMessage = message;
            errorCount++;
        }
    }
}
