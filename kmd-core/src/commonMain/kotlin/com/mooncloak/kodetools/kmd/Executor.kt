package com.mooncloak.kodetools.kmd

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

/**
 * A sealed interface representing the execution mechanism for various asynchronous operations.
 * Implementations of this interface define specific execution patterns such as async-await or reactive flow.
 */
public sealed interface Executor

/**
 * Represents an asynchronous execution mechanism for operations.
 * Provides methods to execute tasks in an asynchronous manner
 * and retrieve results through deferred computation or suspension.
 *
 * @param Result The type of the result produced by the asynchronous operation.
 */
public interface AsyncExecutor<Result> : Executor {

    /**
     * Initiates an asynchronous operation and returns a deferred result.
     * The operation is executed using the mechanism provided by the implementing class.
     *
     * @return A [Deferred] representing the future computation of the result,
     * which can be awaited or further processed as needed.
     */
    public fun async(): Deferred<Result>

    /**
     * Suspends until the asynchronous operation being executed completes and returns the result.
     *
     * @return The result of the asynchronous operation once it is completed successfully.
     */
    public suspend fun await(): Result

    public companion object
}

/**
 * Represents a specialized executor for handling asynchronous operations that return results as a Flow.
 * This interface extends the generic `Executor` interface to support stream-based execution patterns.
 *
 * @param Result The type of result emitted by the flow.
 */
public interface FlowExecutor<Result> : Executor {

    /**
     * Initiates a flow-based operation and returns a stream of results wrapped in a Flow.
     *
     * @return A Flow emitting the results of the asynchronous operation.
     */
    public fun flow(): Flow<Result>

    public companion object
}
