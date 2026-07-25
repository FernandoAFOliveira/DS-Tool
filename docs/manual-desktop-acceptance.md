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

## Milestone 2 state preservation

- [ ] Select a data structure, then change a ranking weight without making that structure ineligible; the same structure remains selected.
- [ ] Change an answer so the selected structure becomes ineligible; the selection clears and stale structure content is not retained.
- [ ] Open question help, switch through every theme, and confirm the same question help remains displayed.
- [ ] Select a structure, switch through every theme, and confirm its selection, explanation, and diagram remain displayed.
- [ ] Select key-value mapping; duplicate preference becomes **Any** and its controls are disabled. Change away from key-value mapping; duplicate controls become usable again.
- [ ] After setting non-default answers, weights, selection, and theme, choose **Subject > Java (active)** and **Experience > Advisor (active)**; all Advisor state remains unchanged.
- [ ] Choose **File > Reset selections**; answers, weights, selection, and navigation reset while the active theme remains unchanged.
- [ ] After reset, make another selection successfully.

## Milestone 3 subject providers

- [ ] On startup, Java remains active and the recommendation list contains the existing Java structures: ArrayList, Stack, Queue, PriorityQueue, ArrayDeque, HashSet, TreeSet, HashMap, and TreeMap.
- [ ] Set non-default answers and weights, select a Java structure, choose a non-default theme, and navigate to question help.
- [ ] Choose **Subject > Java (active)**; the Java-active dialog appears and all prepared state remains unchanged.
- [ ] Milestone 7 supersedes the original C placeholder: switch to C and back
  to Java, confirming the prepared abstract selection and unrelated state
  remain unchanged.
- [ ] Choose **C++** and **Python**; the exact standard not-enabled message
  appears and all prepared state remains unchanged.
- [ ] After each unavailable-subject dialog, change an Advisor answer or theme and confirm the application remains usable.
- [ ] After all subject commands, select a Java structure and confirm its explanation and diagram still load.
- [ ] No subject command changes the active experience or resets locale, accessibility, appearance, navigation, answers, weights, or selection.

## Milestone 4 Knowledge Core

- [ ] On startup, the visible Java recommendation catalog remains exactly ArrayList, Stack, Queue, PriorityQueue, ArrayDeque, HashSet, TreeSet, HashMap, and TreeMap; LinkedList is not added.
- [ ] With all answers and weights at their defaults, the visible order is HashMap, HashSet, ArrayList, ArrayDeque, TreeMap, TreeSet, Queue, Stack, and PriorityQueue.
- [ ] Select **Need key-value mapping? > Yes** and **Need sorted order? > Yes**; TreeMap is the only recommendation.
- [ ] Reset, then select **Need index access? > Yes**; ArrayList is the only recommendation.
- [ ] Reset, then select **Allow duplicates? > No**; HashSet and TreeSet are the only recommendations in that order.
- [ ] Reset, then select **Removal order > Priority**; PriorityQueue is the only recommendation.
- [ ] Select each of the nine Java representations and confirm its existing Java name, explanation, and diagram load without exposing an internal abstract identifier or resource path.
- [ ] Select a structure, change a weight without making it ineligible, and confirm the same abstract selection remains visibly selected.
- [ ] Select a structure, navigate to question help, reselect **Subject > Java (active)**, and confirm answers, weights, selection, navigation, and theme remain unchanged.
- [ ] Switch Java and C after preparing non-default Advisor state; the selected
  abstract structure and unrelated state remain unchanged. C++ and Python
  remain unavailable.
- [ ] Trigger a controlled representation failure in a development session; confirm the standard generic error, full developer logging, and successful use of another command afterward.

## Milestone 5 Explorer experience

- [ ] Start with no selected structure, choose **Experience > Explorer**, and confirm Explorer opens with welcome content and no highlighted structure.
- [ ] Confirm Explorer lists all nine Knowledge Core structures using the Java names ArrayList, Stack, Queue, PriorityQueue, ArrayDeque, HashSet, TreeSet, HashMap, and TreeMap, regardless of the current Advisor answers.
- [ ] Confirm the Explorer list has comfortable row padding while remaining compact; selection, keyboard navigation, and scrolling still work.
- [ ] Select each Explorer structure and confirm the concept, Java representation, strengths, weaknesses, supported operations, complexity, memory and iteration information, common use cases, related structures, and diagram appear.
- [ ] Confirm every Explorer diagram is horizontally centered without unexplained empty space on the right.
- [ ] Drag the Explorer diagram/details splitter in both directions; both panes resize continuously and remain usable.
- [ ] Confirm Explorer headings and body text are left-aligned, section spacing is clear, and no content section is missing.
- [ ] Confirm Explorer content does not expose an internal `StructureId`, resource path, exception detail, or stack trace.
- [ ] Select a structure in Advisor, enter Explorer, and confirm the same shared structure is highlighted and displayed.
- [ ] In Advisor, set restrictive answers that exclude some structures. Enter Explorer and confirm excluded recommendations remain available to browse.
- [ ] Select an Advisor-ineligible structure in Explorer, return to Advisor, and confirm Advisor remains filtered by its existing answers without adding the selected Explorer structure to its recommendation list.
- [ ] Before entering Explorer, prepare non-default answers, weights, theme, and question-help navigation. Select a structure in Explorer, return to Advisor, and confirm answers, weights, subject, theme, preferences, and the saved Advisor navigation page remain unchanged.
- [ ] Switch repeatedly between Advisor and Explorer; the active menu label, shared selection, and both experiences remain usable.
- [ ] Change each theme while Explorer is active; list selection, details, and diagram remain readable and the selection is preserved when returning to Advisor.
- [ ] At normal and high-DPI scaling, repeat the Explorer list, diagram, details, and all-theme checks; text remains readable and unclipped.
- [ ] Choose **File > Reset selections** while Explorer is active; Explorer returns to welcome, Advisor answers and weights reset, and the active theme remains unchanged.
- [ ] Trigger a controlled Explorer failure in a development session; confirm the standard generic error, full developer logging, unchanged prior selection, and successful use of Advisor afterward.
- [ ] Return to Advisor and confirm its answers, filtering, selection, diagram, explanation, and navigation behavior are unchanged.
- [ ] Recheck Java and C switching, C++ and Python unavailable behavior, and a
  Learn command; active-subject state and exact unavailable-feature behavior
  are correct.

## Milestone 6 Learn foundation

- [ ] Choose **Experience > Learn > Flash Cards** and confirm a concept-first card opens with its answer hidden and the menu label changes to **Flash Cards (active)**.
- [ ] Confirm the first card asks about Dynamic array without using `ArrayList` as the concept identity.
- [ ] Reveal the first answer; confirm the Knowledge Core description is primary and the Java `ArrayList` representation appears only as supplementary information.
- [ ] Navigate through all nine cards in stable Knowledge Core order; Previous is disabled on the first card, Next is disabled on the last card, and no card exposes an internal `StructureId`.
- [ ] Reveal several cards; the reviewed count increases once per distinct card and repeated visits do not double-count progress.
- [ ] On a revealed card, switch to Advisor and Explorer, then return to Flash Cards; the same card, revealed answer, and reviewed count are restored.
- [ ] Before studying, prepare non-default Advisor answers, weights, shared structure selection, theme, and question-help navigation. Navigate and reveal Flash Cards, then confirm all prepared Advisor and Explorer state remains unchanged.
- [ ] Select a structure in Advisor or Explorer, navigate to a different Flash Card, and confirm returning to Advisor or Explorer restores the original shared structure rather than the current card.
- [ ] Change every theme while Flash Cards is active; prompt, answer, progress, focus, and buttons remain readable and the session is preserved.
- [ ] At normal and high-DPI scaling, resize the window and confirm card content remains readable, scrollable, and unclipped.
- [ ] Choose **File > Reset selections** while Flash Cards is active; accepted Advisor state resets while the current Flash Card and reviewed progress remain unchanged.
- [ ] Trigger a controlled Flash Cards rendering failure in a development session; confirm the standard generic error, full developer logging, unchanged card/progress state, and successful Advisor use afterward.
- [ ] Confirm **Experience > Learn > Timed Quiz** still displays exactly `This feature is not enabled yet.`
- [ ] Recheck Advisor, Explorer, Java and C switching, and C++ and Python
  unavailable behavior after using Flash Cards.

## Milestone 7 second subject

- [ ] Prepare non-default Advisor answers and weights, select a structure,
  choose a non-default theme, and navigate to question help.
- [ ] Enter Explorer, confirm the shared selection, then navigate and reveal a
  Flash Card so Learn session and progress state are also non-default.
- [ ] Return to each experience and switch from Java to C. The requested
  experience renders successfully before **C (active)** appears in the Subject
  menu.
- [ ] Confirm the selected abstract structure, Advisor answers and weights,
  theme, locale and accessibility behavior, Advisor navigation, active
  experience, current Flash Card, reveal state, and reviewed count are
  preserved.
- [ ] In Advisor, confirm recommendation filtering and ordering are unchanged
  while names use C implementation strategies such as Dynamic array, Binary
  heap, Circular-buffer queue, Hash table, and Balanced-tree map.
- [ ] Select each C recommendation. Its conceptual diagram and a compact
  Knowledge Core-derived explanation load without a Java API page, internal
  `StructureId`, resource path, or exception detail.
- [ ] Confirm the C explanation states that its representation is an
  implementation strategy rather than a standardized library type.
- [ ] In Explorer, confirm all nine Knowledge Core concepts remain available
  and show C terminology while conceptual content, relationships, diagrams,
  and the shared selection remain unchanged.
- [ ] In Flash Cards, reveal answers and confirm the concept remains primary
  while supplementary representation text changes from Java to C.
- [ ] Switch back to Java from Advisor, Explorer, and Flash Cards. Existing
  Java names, Java explanation pages, selection, session, and progress return
  without state loss.
- [ ] Repeat Java/C switching with every theme and at normal and high-DPI
  scaling; content remains readable, scrollable, and usable.
- [ ] Choose C++ and Python; each displays exactly
  `This feature is not enabled yet.` and does not change the active subject or
  unrelated state.
- [ ] Confirm Timed Quiz still displays exactly
  `This feature is not enabled yet.`
- [ ] In a development session, make the active-experience render step fail
  during a Java-to-C switch. Confirm the generic feature-error dialog, full
  developer logging, Java remaining active, all unrelated state remaining
  unchanged, and a successful later subject switch or command.

## Every visible menu command

Dismiss each dialog before continuing.

### Subject

- [ ] **Subject > Java (active)** displays `Java is the active subject.`
- [ ] **Subject > C** activates C, changes its label to **C (active)**, and
  updates the current experience without resetting unrelated state.
- [ ] **Subject > Java** restores Java, changes its label to
  **Java (active)**, and preserves the same abstract selection and unrelated
  state.
- [ ] **Subject > C++** displays exactly `This feature is not enabled yet.`
- [ ] **Subject > Python** displays exactly `This feature is not enabled yet.`

### Experience

- [ ] **Experience > Advisor (active)** displays `Advisor is the active experience.`
- [ ] **Experience > Explorer** opens Explorer and changes its label to **Explorer (active)**.
- [ ] **Experience > Advisor** returns to Advisor and changes its label to **Advisor (active)**.
- [ ] **Experience > Learn > Flash Cards** opens or restores Flash Cards and changes its label to **Flash Cards (active)**.
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
