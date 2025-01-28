package com.mooncloak.kodetools.kmd

/**
 * Represents a mechanism for chaining or concatenating objects of a specific type, `Input`,
 * in an ordered manner to produce a result of type `Output`.
 *
 * This interface defines a contract for objects that can combine or append elements,
 * ensuring a sequential and ordered concatenation process. Implementations of this interface
 * are responsible for managing how the combination is performed and the result is produced.
 *
 * @param [Input] The type of the elements that are concatenated.
 *
 * @param [Output] The type of the result produced after the concatenation.
 */
public interface OrderedConcatenation<Input, Output> {

    /**
     * Combines or appends the given input object with the current instance to produce a result of type `Output`.
     *
     * This method allows chaining or sequentially concatenating elements of type `Input` in an ordered manner.
     * Implementation details of how the combination is performed are defined by the implementing class.
     *
     * @param [other] The input object of type `Input` to be combined with the current instance.
     *
     * @return The result of the combination as an object of type `Output`.
     */
    public fun then(other: Input): Output

    public companion object
}
