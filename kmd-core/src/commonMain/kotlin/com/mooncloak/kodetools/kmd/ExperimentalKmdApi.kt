package com.mooncloak.kodetools.kmd

/**
 * Annotation for marking experimental APIs in the Kmd library.
 *
 * This annotation is used to label specific components of the Kmd library as experimental,
 * meaning their API is subject to change without notice. The use of such APIs requires
 * opting in with explicit acknowledgment of potential instability.
 *
 * Applying this annotation indicates that the marked API may evolve over time, and
 * developers using it should be prepared to accommodate breaking changes.
 *
 * This annotation employs the `RequiresOptIn` mechanism at the WARNING level,
 * prompting developers to explicitly opt-in to utilize these experimental features.
 */
@MustBeDocumented
@RequiresOptIn(
    level = RequiresOptIn.Level.WARNING,
    message = "This API is experimental. It may be changed in the future without notice."
)
public annotation class ExperimentalKmdApi public constructor()
