# Calcify

### Adaptive Arithmetic Speed & Accuracy Trainer

Calcify is an Android application designed to improve arithmetic speed, calculation accuracy, numerical fluency, and confidence under time pressure.

The goal is not simply to solve more questions. It is to help users calculate faster without sacrificing accuracy, recognize weaknesses, build stronger numerical foundations, and measure their improvement over time.

Calcify will combine focused arithmetic practice, timed challenges, adaptive difficulty, performance analysis, and persistent session history in a clean, distraction-free interface.

> **Project status:** Initial repository scaffold and first Kotlin implementation have been added. The Compose dashboard, basic integer-question generator, scoring rules, and starter unit tests are present. The app has not yet been built or tested in Android Studio; later modes, session gameplay, persistence, timers, and adaptive practice remain planned.

---

## 1. Project Goals

Calcify is being developed to:

- Improve calculation speed while maintaining accuracy.
- Strengthen mental arithmetic and numerical fluency.
- Practise arithmetic operations individually and in combined expressions.
- Develop confidence with decimals, fractions, recurring quotients, and rounding.
- Simulate time pressure through timed calculation challenges.
- Identify weak operations and provide targeted practice.
- Track progress through scores, accuracy, response time, session history, and personal records.
- Encourage consistent practice through daily goals and streaks.

The central principle is **measurable improvement through deliberate practice**, rather than repetitive calculations without feedback.

## 2. Training Modes

Calcify is planned to include six primary training modes.

### 2.1 Fast Arithmetic
Quick calculations involving addition, subtraction, multiplication, and division. Difficulty will increase as performance improves.

### 2.2 Mixed Calculations
Expressions involving multiple arithmetic operations, brackets, negative numbers, and the correct order of operations (BODMAS).

### 2.3 Division Mastery
Focused practice with exact division, decimal quotients, recurring decimals, larger divisors, and mathematical rounding.

The answer checker must accept valid approximations at the precision permitted by each question. For example, `28.27` should be accepted for `85 ÷ 3` when rounding to two decimal places is allowed.

### 2.4 Calculation Sprint
Timed challenges in which users solve as many questions as possible before the countdown expires. Performance will include score, accuracy, and response time.

### 2.5 Exam Arithmetic
Numerical practice covering relevant mathematical skills such as fractions, decimals, percentages, squares, square roots, ratios, and other supported calculation types.

### 2.6 Adaptive Practice
A personalized mode that uses previous performance to select suitable questions, increase or decrease difficulty, and target weaker operations.

Adaptive difficulty should also be available within individual training modes where appropriate.

## 3. Scoring System

The initial scoring rules are:

| Answer outcome | Points |
|---|---:|
| Correct answer | +10 |
| Incorrect answer | −2 |
| Skipped question | 0 |

Scores may become negative. Each new session starts at zero.

- **Session score:** Total points earned or lost in one completed session.
- **Daily score:** Sum of completed session scores for the local calendar day.
- **All-time score:** Sum of all completed session scores.
- **Accuracy:** Calculated independently of points.
- **Response time:** Measures the time taken to answer questions.
- **Personal records:** Track best performances and other defined achievements.

Skipped questions must not be counted as answered questions when calculating answer accuracy.

Daily goals, streaks, and possible streak bonuses will be defined separately so that they do not silently alter the core scoring rules.

## 4. Answer Validation and Mathematical Correctness

Calcify must prioritize mathematical correctness over superficial string matching.

The answer-checking system should:

- Accept valid integer and decimal inputs.
- Handle insignificant trailing zeros correctly.
- Support explicitly permitted rounded answers.
- Use decimal arithmetic and explicit rounding rules where appropriate.
- Apply BODMAS correctly when evaluating expressions.
- Reject malformed inputs safely.
- Handle division by zero and other invalid expressions without crashing.
- Avoid unreliable floating-point equality checks.
- Prevent overly broad tolerances from accepting mathematically inaccurate answers.

For recurring decimals, the accepted precision and rounding policy must be defined explicitly. The implementation should distinguish a correctly rounded answer from an approximation that is too imprecise.

## 5. Adaptive Difficulty and Performance Analysis

Calcify should adjust practice according to evidence from the user's performance.

The system will consider factors such as:

- Recent accuracy.
- Correct and incorrect answers.
- Response time.
- Performance by arithmetic operation.
- Difficulty level and sample size.
- Repeated errors and areas requiring revision.

Difficulty should increase only after sufficient consistent performance. Poor accuracy may trigger easier questions or targeted practice.

A small number of answers should not cause unstable difficulty changes.

The performance-analysis system will eventually support personalized recommendations, weakness detection, progress charts, and personal records.

## 6. Home Screen and User Interface

Calcify will use a dark, minimal interface designed for readability and quick access to training.

### Main dashboard

The home screen is planned to contain:

1. **Top bar:** Calcify branding and a settings shortcut.
2. **Progress overview:** Today's score and a compact activity or goal indicator.
3. **Start Training:** The primary action for beginning a session.
4. **Training modes:** Cards for Fast Arithmetic, Mixed Calculations, Division Mastery, and Calculation Sprint, with access to additional modes.
5. **Recommended practice:** A personalized focus area when sufficient performance data exists.
6. **Bottom navigation:** Home, Progress, and History.

New users should see helpful empty states rather than fabricated statistics.

### Visual principles

- Dark charcoal background with slightly lighter grey cards.
- Restrained teal or cyan accents for important actions and progress.
- Clear typography and readable numerical inputs.
- Consistent spacing and accessible touch targets.
- Minimal animations and visual distractions.
- Layouts that remain usable on different screen sizes.

The interface should make starting a session effortless without sacrificing access to useful performance information.

## 7. Technical Stack

The planned implementation uses:

- **Language:** Kotlin
- **Platform:** Android
- **UI:** Jetpack Compose
- **Architecture:** Layered architecture with clear package responsibilities
- **Local persistence:** Room
- **Preferences:** Local persistent settings
- **Testing:** Unit tests and Android instrumentation tests where needed

Core training must work offline. A backend or account system is not required for the initial version.

Dependencies should be introduced only when they solve a concrete requirement.

## 8. Architecture

The project will begin as a single Android application module, organized into distinct packages.

```text
com.sameer.calcify
├── app/
│   ├── ArithmeticTrainerApplication.kt
│   ├── MainActivity.kt
│   └── navigation/
├── domain/
│   ├── model/
│   ├── engine/
│   ├── scoring/
│   ├── session/
│   ├── difficulty/
│   ├── statistics/
│   └── repository/
├── data/
│   ├── local/
│   ├── repository/
│   └── preferences/
├── feature/
│   ├── home/
│   ├── training/
│   ├── results/
│   ├── history/
│   ├── statistics/
│   └── settings/
└── ui/
    └── theme/
```

Tests will live under the corresponding `app/src/test/` and `app/src/androidTest/` source sets.

### Architectural responsibilities

**Domain layer**
- Owns arithmetic rules, question generation, answer checking, scoring, session rules, adaptive difficulty, and performance calculations.
- Uses pure Kotlin wherever practical.
- Must not depend on Compose, Room, or UI implementation details.

**Data layer**
- Implements repository contracts.
- Handles Room persistence, session records, data mapping, and local preferences.

**Presentation layer**
- Contains Compose screens and ViewModels.
- Displays state and forwards user actions to the appropriate domain logic.
- Must not access database DAOs directly.

**UI theme**
- Defines reusable colors, typography, and visual styling independently of business logic.

Each file should have a clear responsibility. The project should not grow into a collection of oversized files containing unrelated logic.

## 9. Development Roadmap

Development will proceed through sequential milestones. Each milestone must pass its acceptance checks before the next major feature begins.

| Milestone | Objective |
|---|---|
| M0 | Verify project configuration, package structure, navigation, theme, and clean build |
| M1 | Implement and test question models, arithmetic generation, and answer checking |
| M2 | Implement scoring, accuracy, and session rules |
| M3 | Build the first playable training interface |
| M4 | Add timers, sprint rules, and lifecycle-safe session handling |
| M5 | Persist sessions and calculate daily and all-time scores |
| M6 | Implement adaptive difficulty and targeted practice |
| M7 | Add history, statistics, weakness analysis, and personal records |
| M8 | Perform regression testing, accessibility checks, and release preparation |

### Milestone acceptance principle

A feature is not considered complete merely because it appears to work on screen. Its relevant logic must be tested, edge cases handled, the application built successfully, and existing functionality preserved.

## 10. Testing Strategy

Testing will cover both ordinary use and edge cases.

Examples include:

- Correct evaluation of arithmetic expressions.
- Division by zero and recurring decimal handling.
- Correct rounding and rejection of invalid answers.
- Negative session scores.
- Skipped questions and accuracy calculations.
- Duplicate-answer prevention.
- Timer expiry and attempts to answer after a session ends.
- Database persistence after restarting the application.
- Correct aggregation of daily and all-time scores.
- Adaptive difficulty with insufficient data.
- Empty history, small screens, and interrupted sessions.

Pure mathematical and scoring logic should be tested independently of the UI wherever possible.

## 11. Development Principles

Calcify will be developed incrementally with the following rules:

1. Understand the design and responsibilities before writing code.
2. Implement one coherent feature or milestone at a time.
3. Keep business logic separate from UI and persistence.
4. Build and test after meaningful changes.
5. Diagnose failures by category and isolate the smallest responsible component.
6. Avoid adding abstractions, dependencies, or features without a clear need.
7. Preserve stable checkpoints with version control.
8. Document important decisions and update this README when the architecture or scope changes.
9. Ensure that the developer understands the code rather than relying on unexamined generated implementations.

## 12. Definition of Done

A feature is complete when:

- Its purpose and responsibilities are clear.
- Inputs, outputs, and important state transitions are defined.
- Normal and edge cases have appropriate tests.
- The application compiles and relevant tests pass.
- Existing functionality has not regressed.
- Data and session state behave correctly.
- The implementation remains understandable and maintainable.

## 13. Current Status

The architecture, major training modes, scoring rules, UI direction, and development roadmap have been specified.

Implementation has not yet been confirmed complete.

**Immediate next step:** Verify the existing Android Studio project configuration, establish the initial app entry point and navigation, and confirm a clean build before implementing the arithmetic engine.

---

Calcify is intended to become a reliable, offline-first arithmetic trainer that measures real improvement in speed, accuracy, and numerical confidence—not merely a calculator with a score counter.

