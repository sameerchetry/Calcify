# Calcify — App Specification

## Purpose
Calcify is an offline-first Android app for improving arithmetic speed, accuracy, numerical fluency, and confidence under time pressure.

## Platform and architecture
- Android application written in Kotlin with Jetpack Compose.
- Application ID/package: `com.sameer.calcify`.
- Single app module.
- Layered architecture: presentation, domain, and data.
- Room persistence is planned for session history and progress.
- UI screens must not access DAOs directly.
- Core arithmetic and scoring rules should be pure Kotlin and independently testable.

## Training modes
Implementation order:
1. Fast Arithmetic
2. Division Mastery
3. Mixed Calculations (BODMAS)
4. Calculation Sprint
5. Exam Arithmetic
6. Adaptive Practice

Planned coverage includes the four arithmetic operations, brackets and order of operations, decimal and recurring division, fractions, percentages, squares, square roots, ratios, and timed practice.

## Scoring rules
- Correct answer: +10 points.
- Incorrect answer: −2 points.
- Skipped question: 0 points.
- Each session starts at zero; a session score may be negative.
- Daily score is the sum of completed session scores on the user's local calendar day.
- All-time score is the sum of all completed session scores.
- Skipped questions are excluded from the answered-question denominator for accuracy.
- Streaks, goals, and bonuses must be specified separately and must not silently change these base rules.

## Answer validation
Decimal answers must be checked against the precision permitted by the question. For example, `28.27` should be accepted for `85 ÷ 3` when rounding to two decimal places is permitted. Use explicit decimal arithmetic and rounding rules; avoid overly broad tolerances that accept materially incorrect answers.

## Design direction
- Dark charcoal/grey foundation.
- Slightly lighter cards and surfaces.
- Restrained teal/cyan accent.
- Clear hierarchy, readable question display, and minimal distractions.
- Home, training, progress, and history destinations.

## Planned home screen
- App title and settings entry.
- Today's activity/score summary.
- Start Training action.
- Training mode cards.
- Recommended practice.
- Bottom navigation for Home, Progress, and History.

## Milestones
- M0: Project setup, navigation, theme, clean build.
- M1: Pure Kotlin arithmetic engine and unit tests.
- M2: Session state and scoring.
- M3: Playable training UI.
- M4: Timers and sprint behaviour.
- M5: Room persistence and repositories.
- M6: Adaptive difficulty.
- M7: Progress, history, and statistics.
- M8: Regression tests, accessibility, and release preparation.

## Status
This file describes intended behaviour. A feature is not considered implemented until its code and tests exist and have been verified.
