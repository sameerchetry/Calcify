# Calcify

### Adaptive Arithmetic Speed & Accuracy Trainer

Calcify is an offline-first Android app for improving calculation speed, accuracy, numerical fluency, and confidence under time pressure.

## Implemented features

- **Fast Arithmetic:** ten-question sessions covering addition, subtraction, multiplication, and exact integer division.
- **Division Mastery:** ten-question sessions with division answers rounded to two decimal places.
- **Mixed Calculations:** ten-question sessions with combined operations, multiplication precedence, and parentheses (BODMAS).
- **Calculation Sprint:** a 60-second timed challenge with a continuous stream of arithmetic questions.
- **Scoring:** correct +10, incorrect −2, skipped 0. Session scores can be negative.
- **Feedback:** submitted answers are checked once; incorrect responses reveal the expected answer.
- **Local session history:** completed sessions are saved to a Room database on the device.
- **Progress dashboard:** daily and all-time points, number of completed sessions, best session score, average accuracy, and the training mode with the lowest average accuracy.
- **History screen:** saved session date, mode, score, accuracy, question count, duration, and correct/incorrect/skipped totals.

## Scoring and accuracy

| Outcome | Points |
|---|---:|
| Correct | +10 |
| Incorrect | −2 |
| Skipped | 0 |

Accuracy is calculated among answered questions; skipped questions are excluded. Division Mastery uses decimal arithmetic and HALF_UP rounding to two decimal places. Completed sessions are stored locally and do not require an account or network connection. Exiting a session before completion does not save it.

## Technical stack

- Kotlin and Jetpack Compose
- Room for local session persistence
- Pure Kotlin arithmetic generation, answer checking, and scoring
- JUnit unit tests for arithmetic and scoring rules

## Development note

The code has been committed to the GitHub `main` branch for local build and testing. The new Room/KSP dependency and Compose screens still need to be compiled and verified in Android Studio on the development machine before this milestone should be considered fully tested.
