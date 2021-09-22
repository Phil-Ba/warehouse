package at.adverity.warehouse.rest.model

class AggregateResult(val result: String,
                      val value: String
) {
    constructor(result: String,
                value: Long
    ) : this(
        result,
        value.toString()
    )

    constructor(vararg result: String) : this(
        result.dropLast(1)
            .toString(),
        result.last()
    )
}