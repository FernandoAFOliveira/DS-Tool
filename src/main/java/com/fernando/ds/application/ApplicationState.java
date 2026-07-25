package com.fernando.ds.application;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import com.fernando.ds.library.QuestionInfo;
import com.fernando.ds.knowledge.StructureId;
import com.fernando.ds.model.DSRequirements;
import com.fernando.ds.model.Preference;
import com.fernando.ds.model.RemovalOrder;
import com.fernando.ds.subject.SubjectId;

/**
 * Owns the user state shared across DS-Tool experiences and presentation layers.
 *
 * <p>The desktop experiences share this state while subject-specific
 * information is supplied through a provider.</p>
 */
public final class ApplicationState {

    public enum Experience {
        ADVISOR,
        EXPLORER
    }

    public enum Appearance {
        LIGHT,
        SOFT_BLUE,
        DARK,
        DARK_BLUE
    }

    public enum NavigationKind {
        WELCOME,
        QUESTION,
        DATA_STRUCTURE,
        MESSAGE
    }

    public record AccessibilityPreferences(
        boolean highContrast,
        boolean reducedMotion
    ) {
    }

    public record Navigation(
        NavigationKind kind,
        QuestionInfo.QuestionId questionId,
        String title,
        String message
    ) {
        public Navigation {
            Objects.requireNonNull(kind, "kind");

            if (kind == NavigationKind.QUESTION && questionId == null) {
                throw new IllegalArgumentException(
                    "Question navigation requires a question"
                );
            }
            if (kind == NavigationKind.MESSAGE
                && (title == null || message == null)) {
                throw new IllegalArgumentException(
                    "Message navigation requires a title and message"
                );
            }
        }

        public static Navigation welcome() {
            return new Navigation(
                NavigationKind.WELCOME,
                null,
                null,
                null
            );
        }

        public static Navigation question(QuestionInfo.QuestionId questionId) {
            return new Navigation(
                NavigationKind.QUESTION,
                Objects.requireNonNull(questionId, "questionId"),
                null,
                null
            );
        }

        public static Navigation dataStructure() {
            return new Navigation(
                NavigationKind.DATA_STRUCTURE,
                null,
                null,
                null
            );
        }

        public static Navigation message(String title, String message) {
            return new Navigation(
                NavigationKind.MESSAGE,
                null,
                Objects.requireNonNull(title, "title"),
                Objects.requireNonNull(message, "message")
            );
        }
    }

    private SubjectId activeSubject = SubjectId.JAVA;
    private Experience activeExperience = Experience.ADVISOR;
    private final DSRequirements recommendationAnswers = new DSRequirements();
    private StructureId selectedStructureId;
    private Appearance appearance = Appearance.LIGHT;
    private Navigation navigation = Navigation.welcome();
    private AccessibilityPreferences accessibilityPreferences =
        new AccessibilityPreferences(false, false);
    private Locale locale;

    public ApplicationState() {
        this(Locale.getDefault());
    }

    public ApplicationState(Locale locale) {
        this.locale = Objects.requireNonNull(locale, "locale");
    }

    public SubjectId getActiveSubject() {
        return activeSubject;
    }

    public void setActiveSubject(SubjectId activeSubject) {
        this.activeSubject = Objects.requireNonNull(
            activeSubject,
            "activeSubject"
        );
    }

    public Experience getActiveExperience() {
        return activeExperience;
    }

    public void setActiveExperience(Experience activeExperience) {
        this.activeExperience = Objects.requireNonNull(
            activeExperience,
            "activeExperience"
        );
    }

    public DSRequirements getRecommendationAnswers() {
        return new DSRequirements(recommendationAnswers);
    }

    public void setPreference(
        QuestionInfo.QuestionId questionId,
        Preference preference
    ) {
        Objects.requireNonNull(questionId, "questionId");
        Objects.requireNonNull(preference, "preference");

        switch (questionId) {
            case KEY_VALUE -> {
                recommendationAnswers.setKeyValuePreference(preference);
                if (preference == Preference.YES) {
                    recommendationAnswers.setDuplicatePreference(Preference.ANY);
                }
            }
            case DUPLICATES -> recommendationAnswers.setDuplicatePreference(
                recommendationAnswers.getKeyValuePreference() == Preference.YES
                    ? Preference.ANY
                    : preference
            );
            case SORTED -> recommendationAnswers.setSortedPreference(preference);
            case INDEXED -> recommendationAnswers.setIndexedPreference(preference);
            default -> throw new IllegalArgumentException(
                questionId + " is not a preference question"
            );
        }
    }

    public void setWeight(QuestionInfo.QuestionId questionId, int weight) {
        Objects.requireNonNull(questionId, "questionId");
        if (weight < 0 || weight > 5) {
            throw new IllegalArgumentException("weight must be between 0 and 5");
        }

        switch (questionId) {
            case LOOKUP -> recommendationAnswers.setLookupWeight(weight);
            case ADD_DELETE -> recommendationAnswers.setAddDeleteWeight(weight);
            case MEMORY -> recommendationAnswers.setMemoryWeight(weight);
            default -> throw new IllegalArgumentException(
                questionId + " is not a weight question"
            );
        }
    }

    public void setRemovalOrder(RemovalOrder removalOrder) {
        recommendationAnswers.setRemovalOrderPreference(
            Objects.requireNonNull(removalOrder, "removalOrder")
        );
    }

    public Optional<StructureId> getSelectedStructureId() {
        return Optional.ofNullable(selectedStructureId);
    }

    public void selectStructure(StructureId structureId) {
        selectedStructureId = Objects.requireNonNull(
            structureId,
            "structureId"
        );
    }

    public void clearSelectedStructure() {
        selectedStructureId = null;
    }

    public Appearance getAppearance() {
        return appearance;
    }

    public void setAppearance(Appearance appearance) {
        this.appearance = Objects.requireNonNull(appearance, "appearance");
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public void navigateToWelcome() {
        navigation = Navigation.welcome();
    }

    public void navigateToQuestion(QuestionInfo.QuestionId questionId) {
        navigation = Navigation.question(questionId);
    }

    public void navigateToDataStructure() {
        navigation = Navigation.dataStructure();
    }

    public void navigateToMessage(String title, String message) {
        navigation = Navigation.message(title, message);
    }

    public AccessibilityPreferences getAccessibilityPreferences() {
        return accessibilityPreferences;
    }

    public void setAccessibilityPreferences(
        AccessibilityPreferences accessibilityPreferences
    ) {
        this.accessibilityPreferences = Objects.requireNonNull(
            accessibilityPreferences,
            "accessibilityPreferences"
        );
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = Objects.requireNonNull(locale, "locale");
    }

    /** Resets Advisor-owned session state while preserving global preferences. */
    public void resetAdvisorSession() {
        recommendationAnswers.reset();
        selectedStructureId = null;
        navigation = Navigation.welcome();
    }
}
