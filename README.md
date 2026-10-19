# Calcify

### Adaptive Arithmetic Speed & Accuracy Trainer

Calcify is an Android application designed to improve arithmetic speed, calculation accuracy, numerical fluency, and confidence under time pressure.

The goal is not simply to solve more questions. It is to help users calculate faster without sacrificing accuracy, recognize weaknesses, build stronger numerical foundations, and measure their improvement over time.

## Current implementation

The dashboard is connected to a first playable training flow.

- **Fast Arithmetic:** ten-question sessions with addition, subtraction, multiplication, and exact integer division.
- **Division Mastery:** ten-question sessions with decimal quotients checked at two decimal places.
- **Scoring:** correct +10, incorrect −2, skipped 0; session scores can be negative.
- **Feedback:** each submitted answer is checked once, with the correct answer shown for an incorrect response.
- **Session summary:** displays score, accuracy, and correct/incorrect/skipped counts.

**Not implemented yet:** persistent session history, daily/all-time aggregation, timers, Mixed Calculations/BODMAS, Exam Arithmetic, adaptive difficulty, and long-term progress statistics. Current session results are held in memory and are not saved after leaving the session or app.

## Scoring rules

| Answer outcome | Points |
|---|---:|
| Correct answer | +10 |
| Incorrect answer | −2 |
| Skipped question | 0 |

Each session starts at zero. Accuracy is calculated among answered questions; skipped questions are excluded. Decimal division answers are rounded using HALF_UP to two decimal places and compared using decimal arithmetic, not floating-point tolerance.

## Technical stack

- Kotlin and Android
- Jetpack Compose
- Pure Kotlin arithmetic generation, answer checking, and scoring
- JUnit unit tests for core rules
- Offline-first; no backend required

## Development milestones

1. Project setup, navigation, and theme
2. Arithmetic question generation and answer validation
3. Session scoring and playable training UI
4. Timer and sprint behaviour
5. Room persistence and session history
6. Adaptive difficulty
7. Progress, weakness analysis, and statistics
8. Regression tests, accessibility, and release preparation

A feature is not considered complete until it is built and tested in Android Studio. This implementation has been committed to the repository for local build and verification; successful Android Studio build/test results have not yet been confirmed.
