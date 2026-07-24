# Desktop manual acceptance checklist

Use this checklist on a workstation with a visible desktop after `mvn clean test` passes. Record the operating system, JDK, Maven version, display scaling, and date with the test result. Do not run a packaging or release script.

## Start and baseline

Run:

```text
mvn javafx:run
```

- [ ] The application opens as **Data Structure Advisor** without an uncaught exception.
- [ ] The main window remains responsive and can be resized down to its minimum size.
- [ ] Questions, the data-structure list, diagram area, explanation area, and menu bar are visible.
- [ ] The initial welcome content and diagram load.

## Advisor behavior and state

- [ ] Change several Yes/No/Any answers; the recommendation list updates.
- [ ] Change lookup, add/delete, and memory weights; the list updates and controls remain responsive.
- [ ] Select a removal order; incompatible structures are filtered.
- [ ] Select a data structure; its diagram and explanation appear.
- [ ] Select **File > Reset selections**; answers and weights return to defaults and welcome content returns.
- [ ] After reset, select another answer or structure successfully.

## Every visible menu command

Dismiss each dialog before continuing.

### Subject

- [ ] **Subject > Java (active)** displays `Java is the active subject.`
- [ ] **Subject > C** displays exactly `This feature is not enabled yet.`
- [ ] **Subject > C++** displays exactly `This feature is not enabled yet.`
- [ ] **Subject > Python** displays exactly `This feature is not enabled yet.`

### Experience

- [ ] **Experience > Advisor (active)** displays `Advisor is the active experience.`
- [ ] **Experience > Explorer** displays exactly `This feature is not enabled yet.`
- [ ] **Experience > Learn > Flash Cards** displays exactly `This feature is not enabled yet.`
- [ ] **Experience > Learn > Timed Quiz** displays exactly `This feature is not enabled yet.`

### View

For each selection, confirm menus, panels, text, selections, explanation content, and the diagram remain readable.

- [ ] **View > Theme > Light** applies the light theme.
- [ ] **View > Theme > Soft Blue** applies the soft-blue theme.
- [ ] **View > Theme > Dark** applies the dark theme.
- [ ] **View > Theme > Dark Blue** applies the dark-blue theme.
- [ ] Switching themes preserves current answers, weights, selected recommendation, and navigation position.

### Help

- [ ] **Help > About** opens an owned About dialog with readable content and no internal error details.
- [ ] Closing About returns focus to the usable main window.

### File exit — perform last

- [ ] **File > Exit** closes the main window cleanly.
- [ ] No unexpected exception is printed when the window closes.

`Learn` and `Theme` are navigation submenus, not feature commands; their leaf items are the commands validated above.

## Reliability and recovery

- [ ] After dismissing every not-enabled dialog, immediately run a working action such as changing the theme; it succeeds.
- [ ] Repeatedly open and close active/not-enabled/About dialogs; no duplicate windows, frozen input, or degraded behavior appears.
- [ ] No user dialog shows a Java exception class, stack trace, file path, resource path, or exception message.
- [ ] Menus and controls remain usable after rapid sequential actions.

To manually validate the true failure path, use a development/debug session to make one guarded feature action throw a `RuntimeException` before its normal work. Do not commit the injected fault.

- [ ] The full exception and stack trace are written to the developer log with the feature name.
- [ ] The only user error text is exactly `There was an error trying to display this feature.`
- [ ] The main window stays open after the error dialog is dismissed.
- [ ] A different action works immediately afterward.

If controlled fault injection is unavailable, mark these four checks **not executed**; the headless `UiActionGuardTest` evidence does not replace visible-dialog acceptance.

## Threading and responsiveness

- [ ] Diagram updates complete without freezing Swing controls.
- [ ] Rapid structure and theme changes do not produce stale dialogs or uncaught Swing/JavaFX thread exceptions.
- [ ] While a diagram is loading, menus and ordinary Swing controls remain responsive.

## Accessibility and presentation

- [ ] All menu commands can be reached and activated by keyboard navigation.
- [ ] Focus indicators are visible.
- [ ] Dialog titles identify the selected feature and dialogs are owned/centered relative to the application.
- [ ] Text is not clipped at the workstation's normal and high-DPI scaling settings.
- [ ] Each theme provides readable foreground/background and selected-item contrast.

## Result record

Record:

```text
Environment:
Date:
Tester:
Automated test result:
Manual result: PASS / FAIL / PARTIAL
Failed or unexecuted item(s):
Notes / log location:
```

Any failed required item blocks acceptance. A partial result must list every unexecuted item and the environment needed to complete it.
