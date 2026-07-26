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
- [ ] Open and return from Timed Quiz; Flash Cards restores the same card,
  reveal state, and reviewed count.
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
- [ ] Start a Java quiz, switch to C, and confirm the quiz remains bound to
  Java while Flash Cards uses C after navigation.
- [ ] In a development session, make the active-experience render step fail
  during a Java-to-C switch. Confirm the generic feature-error dialog, full
  developer logging, Java remaining active, all unrelated state remaining
  unchanged, and a successful later subject switch or command.

## Milestone 8 Explorer subject tabs and code examples

- [ ] Open Explorer with no selected structure. The detail tabs are exactly
  **Overview**, **Java**, and **C**, Overview is selected, and C++ and Python
  are absent.
- [ ] Confirm all three welcome tabs prompt for a structure without showing an
  internal identifier, resource path, or empty content heading.
- [ ] Select each of the nine structures and confirm all three tabs render
  successfully.
- [ ] For every structure, confirm Overview contains only applicable
  language-neutral definition, characteristics, strengths, weaknesses,
  operations, complexity, memory and iteration, use cases, and relationships.
- [ ] Confirm Overview contains no Java class name, Java API, C implementation
  detail, subject-specific code, or active-subject representation.
- [ ] Confirm Java provides a representation name, useful implementation/API
  guidance, common methods, a concise code example, and relevant caveats.
- [ ] Confirm C provides an implementation strategy, relevant struct or helper
  guidance, allocation and ownership considerations, a concise code example,
  and relevant caveats.
- [ ] Confirm C never implies that its strategies are a standardized
  collections framework equivalent to Java.
- [ ] With one structure selected, record the complete Overview, switch the
  active subject between Java and C, and confirm Overview is unchanged.
- [ ] Confirm the left Explorer list changes between Java and C terminology
  while all three detail tabs remain available in the same order.
- [ ] Move among Overview, Java, and C and confirm the abstract diagram remains
  unchanged and visible above the tabs.
- [ ] Select the C tab, then select another structure, change every theme,
  switch to Advisor and back, and switch the active subject. Confirm the C tab
  remains selected throughout.
- [ ] Confirm selecting Java or C detail tabs never changes the active subject
  menu label or left Explorer representation list.
- [ ] Choose **File > Reset selections** while a subject tab is selected.
  Confirm established reset behavior is unchanged and Explorer refreshes to
  welcome without resetting unrelated state.
- [ ] Use keyboard navigation to focus and move among all tabs. Focus
  indicators remain visible and content is reachable without a mouse.
- [ ] At normal and high-DPI scaling, resize the window and drag the Explorer
  splitter. Tab labels, prose, tables, and code remain readable and unclipped.
- [ ] Confirm long code blocks scroll safely without forcing the application
  beyond its minimum usable size.
- [ ] Repeat tab and code readability checks in Light, Soft Blue, Dark, and
  Dark Blue themes.
- [ ] In a development session, inject an Explorer detail rendering
  `RuntimeException` during structure selection. Confirm the generic error
  message, full developer logging, unchanged prior selected structure, and a
  successful later Explorer or Advisor action.
- [ ] Confirm Advisor explanations, Flash Cards behavior, Java/C subject
  switching, and theme preservation remain unchanged.
- [ ] Confirm Timed Quiz opens without changing Explorer tab selection,
  content, or shared structure selection.

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
- [ ] **Experience > Learn > Timed Quiz** opens or restores Timed Quiz and
  changes its label to **Timed Quiz (active)**.

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

## Post-Milestone-8 UI context polish

### Explorer representation list

- [ ] With Java active, all nine representation names are readable without
  unexpected horizontal scrolling.
- [ ] With C active, all nine representation names, including the longest
  array-, queue-, set-, and map-oriented labels, are readable.
- [ ] Drag the divider between the representation list and Explorer content;
  it remains adjustable and both sides remain usable.
- [ ] Switch Java and C repeatedly; the preferred left width refreshes and
  never leaves the list cramped.
- [ ] Manually move the divider, switch subjects, and confirm the manual
  position is not immediately overwritten.

### Persistent subject indicator

- [ ] `Active subject: Java` or `Active subject: C` remains visible in Advisor,
  Explorer, and Flash Cards.
- [ ] Switch Java to C and C to Java; the header updates immediately after
  each successful switch.
- [ ] Check Light, Soft Blue, Dark, and Dark Blue; the indicator remains
  readable and does not dominate the window.
- [ ] Trigger a controlled subject-rendering failure; the generic failure
  message appears and the previous header and active subject remain unchanged.

### Experience-specific context

- [ ] Advisor clearly identifies the subject used for recommendations and
  implementation information; recommendations are unchanged except for
  provider representation names.
- [ ] Explorer states that the active subject controls the representation
  list, while Overview explicitly says `Language-neutral concept overview`.
- [ ] Explorer tabs remain exactly Overview, Java, and C; Java and C tabs,
  shared diagram, and selected local tab continue to work and survive subject
  switches.
- [ ] Flash Cards shows `Subject context: Java` or `Subject context: C`;
  supplementary representation material changes with the subject while the
  current card, reveal state, reviewed progress, and navigation are preserved.

### Timed Quiz and general regression

- [ ] **Experience > Learn > Timed Quiz** opens the pre-session screen and
  displays the current subject and ten-question length.
- [ ] Confirm the roadmap and quiz ADR record that a session captures its
  subject at start and cannot silently change subject mid-session.
- [ ] Recheck keyboard navigation, visible focus, every theme, normal and
  high-DPI scaling, window resizing, clipping, and representation-list
  scrolling.

## Diagram Content Expansion

### Every concept

- [ ] Open all nine concepts in Advisor and Explorer; every primary diagram
  loads and the selected abstract structure remains unchanged.
- [ ] Confirm shared diagrams contain no Java collection class, Java-specific
  API, C allocation function, C struct identifier, import, or include.
- [ ] Confirm every title and label is readable and each primary communicates
  its concept without relying on color alone.

### Visual consistency

- [ ] Compare all diagrams: stored data, focus, operations, notes, warnings,
  and containers use the same semantic styles.
- [ ] Confirm solid arrows consistently mean structural relationships and
  dotted arrows consistently mean operations, movement, or annotations.
- [ ] Confirm titles, typography, spacing, and annotation placement remain
  consistent across the complete set.

### Multi-diagram concepts

- [ ] Dynamic array, queue, priority queue, ordered set, hash map, and ordered
  map show a compact Diagram selector; stack, deque, and hash set do not.
- [ ] Each selector defaults to Structure, switches to its secondary view, and
  returns to Structure using mouse and keyboard.
- [ ] Changing diagrams preserves the selected `StructureId`, active subject,
  Advisor state, and selected Explorer detail tab.
- [ ] Switch Java and C, change theme, and leave/return to the experience; the
  locally selected diagram remains selected where it is still available.
- [ ] With a controlled missing optional resource in a development session,
  the primary diagram appears and the application remains usable.

### Themes and layout

- [ ] Inspect all diagrams in Light, Soft Blue, Dark, and Dark Blue.
- [ ] At the minimum, ordinary, and maximized window sizes, resize the diagram
  area and confirm there is no clipping, unreadable text, excessive whitespace,
  or unexpected horizontal scrolling.

### Required visual-polish pass

- [ ] At 100% and 150% display scaling, open Hash Map in Advisor. Confirm the
  Structure view fills the available diagram area reasonably, its title and
  subtitle stay centered and naturally wrapped, and all labels are readable.
- [ ] In Advisor, switch Hash Map to Collision handling with mouse and
  keyboard. Confirm the selector stays compact and the secondary view refits
  without clipping, scrolling, distortion, or selection/state changes.
- [ ] Repeat both Hash Map views in Explorer. Their scale is comparable to
  Advisor, and Overview, Java, C, the selected tab, and `StructureId` remain
  unchanged.
- [ ] Open Stack in both experiences. Confirm the entire selector row and its
  spacing are absent while the diagram remains centered and uses the
  available area.
- [ ] Inspect every primary and secondary diagram at ordinary, maximized, and
  narrow supported sizes. Confirm titles, labels, centering, and spacing;
  record any excessive unused area or narrow layout.
- [ ] Resize the window and move the Advisor and Explorer diagram splitters.
  Confirm the current SVG continuously refits with one aspect ratio, reasonable
  margins, no clipping, and no normal-use scrollbars.
- [ ] Switch every theme, switch Advisor/Explorer, and switch Java/C while a
  secondary diagram is selected. Confirm the diagram refits and local
  selection is preserved.
- [ ] Confirm concept views do not show the legacy Data Structure Advisor
  banner or logo. Confirm welcome retains compact branding and application
  identity remains in the shell/window title.

### Failure handling

- [ ] With a controlled invalid primary mapping in a development session,
  confirm the generic failure dialog, full feature-context logging, unchanged
  prior structure/subject state, and successful later diagram action.
- [ ] Confirm Advisor, Explorer tabs, Java/C switching, Flash Cards, and the
  working Timed Quiz retain their established behavior.

## Milestone 9 Timed Quiz

### Start

- [ ] With Java active, choose **Experience > Learn > Timed Quiz**. Confirm
  the pre-session screen explains the purpose, previews Java, states
  **10 questions**, and shows **Start Quiz**.
- [ ] Start the quiz. Confirm the label becomes **Quiz subject: Java**, the
  timer begins at 0:00, and Question 1 of 10 appears.
- [ ] Reset or complete the session, switch globally to C, return to the
  pre-session screen, and confirm a new quiz captures C.

### During quiz

- [ ] Complete several questions using the mouse. Selecting a radio choice
  enables Submit but does not submit automatically.
- [ ] Submit once. Confirm immediate Correct/Incorrect feedback, the correct
  answer, and a concise explanation appear.
- [ ] Confirm the selected answers are disabled after submission and a
  deliberate **Next** action is required.
- [ ] Attempt repeated activation after submission; the score and position do
  not change twice.
- [ ] Confirm progress advances exactly one question after Next and the
  elapsed session timer updates without a countdown or automatic failure.

### Subject switching

- [ ] Begin a Java quiz, then choose **Subject > C**. Confirm the global
  indicator changes to C but the quiz label, questions, score, position, and
  timer remain bound to Java.
- [ ] Visit Advisor, Explorer, and Flash Cards; confirm each uses C normally.
  Return to Timed Quiz and confirm its Java session remains intact.
- [ ] Complete that quiz and choose **Start New Quiz**. Confirm the new session
  captures the now-active C subject.

### Completion and reset

- [ ] Submit the tenth answer. Confirm the elapsed time stops and **View
  Results** is required before the summary.
- [ ] Confirm final score, percentage, correct count, incorrect count, and
  elapsed time agree with the submitted answers.
- [ ] Choose **Start New Quiz** and confirm score, progress, answers, and time
  reset for a fresh session.
- [ ] During an active quiz choose **File > Reset selections**. Confirm only
  Timed Quiz returns to its pre-session screen; Advisor answers, Explorer
  selection, Flash Cards session/progress, subject, and theme remain unchanged.

### State isolation and failure recovery

- [ ] Before starting, prepare non-default Advisor answers/navigation,
  Explorer selection/tab, Flash Cards card/reveal/progress, and theme. Start,
  answer, complete, reset, and restart quizzes; all prepared state remains.
- [ ] Trigger a controlled Timed Quiz rendering failure. Confirm the generic
  feature error, full developer log, unchanged prior quiz state, and a
  successful retry or another command immediately afterward.

### Accessibility and layout

- [ ] Complete an entire quiz without a mouse. Tab order is logical; arrow
  keys move within the radio group; Space selects; Enter/Space activates the
  expected focused/default button; and focus moves to the next useful control.
- [ ] Repeat the pre-session, question, feedback, and summary screens in
  Light, Soft Blue, Dark, and Dark Blue themes.
- [ ] At normal and high-DPI scaling, resize from the minimum supported window
  through maximized size. The title, subject, progress, timer, question,
  choices, feedback, and controls remain readable, scrollable, and unclipped.

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
