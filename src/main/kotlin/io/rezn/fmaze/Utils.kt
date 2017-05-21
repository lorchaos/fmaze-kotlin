package io.rezn.fmaze

/**
 * Created by rlorca on 21.05.17.
 */
sealed class Either<L, R> {
    class Left<L, R>(val l: L) : Either<L, R>() {
        override fun toString(): String = "Left $l"
    }

    class Right<L, R>(val r: R) : Either<L, R>() {
        override fun toString(): String = "Right $r"
    }

    fun right() : R?  = if (this is Either.Right<L, R>) this.r else null

    fun left() : L?  = if (this is Either.Left<L, R>) this.l else null

    fun right(block: (R) -> Unit) { if(this is Either.Right<L, R>) block(this.r) }

    infix fun <Rp> bind(f: (R) -> (Either<L, Rp>)): Either<L, Rp> {
        return when (this) {
            is Either.Left<L, R> -> Left<L, Rp>(this.l)
            is Either.Right<L, R> -> f(this.r)
        }
    }

    infix fun <Rp> seq(e: Either<L, Rp>): Either<L, Rp> = e

    companion object {
        fun <L, R> value(a: R) = Either.Right<L, R>(a)
        fun <Error, R> fail(err: Error) = Either.Left<Error, R>(err)
    }
}

data class Error(val msg: String)