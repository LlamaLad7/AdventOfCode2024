package utils.z3

import com.microsoft.z3.ArithExpr
import com.microsoft.z3.ArithSort
import com.microsoft.z3.BoolExpr
import com.microsoft.z3.BoolSort
import com.microsoft.z3.Context
import com.microsoft.z3.Expr
import com.microsoft.z3.IntExpr
import com.microsoft.z3.IntNum
import com.microsoft.z3.IntSort
import com.microsoft.z3.Model
import com.microsoft.z3.Optimize
import com.microsoft.z3.Sort
import com.microsoft.z3.Status

abstract class Z3Scope(private val context: Context) {
    protected abstract val model: Model

    val Number.z3Int: IntExpr get() = context.mkInt(this.toLong())
    val String.z3Int: IntExpr get() = context.mkIntConst(this)
    val Expr<IntSort>.solution get() = (model.getConstInterp(this) as IntNum).int64

    infix fun <T : Sort> Expr<T>.eq(other: Expr<T>): BoolExpr = context.mkEq(this, other)
    operator fun <T : ArithSort> Expr<T>.plus(other: Expr<T>): ArithExpr<T> = context.mkAdd(this, other)
    operator fun <T : ArithSort> Expr<T>.times(other: Expr<T>): ArithExpr<T> = context.mkMul(this, other)
}

class OptimizeScope(private val impl: Optimize, context: Context) : Z3Scope(context) {
    override val model: Model get() = impl.model

    fun constraints(vararg conditions: Expr<BoolSort>) = impl.Assert(*conditions)

    fun <R : Sort> minimize(expr: Expr<R>) {
        impl.MkMinimize(expr)
    }

    fun solve() = impl.Check() == Status.SATISFIABLE
}

fun <T> z3Optimize(setup: OptimizeScope.() -> T): T {
    Context().use { ctx ->
        val scope = OptimizeScope(ctx.mkOptimize(), ctx)
        return scope.setup()
    }
}