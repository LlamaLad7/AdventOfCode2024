package utils.z3

import com.microsoft.z3.ArithExpr
import com.microsoft.z3.ArithSort
import com.microsoft.z3.BitVecExpr
import com.microsoft.z3.BitVecNum
import com.microsoft.z3.BitVecSort
import com.microsoft.z3.BoolExpr
import com.microsoft.z3.BoolSort
import com.microsoft.z3.Context
import com.microsoft.z3.Expr
import com.microsoft.z3.IntExpr
import com.microsoft.z3.IntNum
import com.microsoft.z3.IntSort
import com.microsoft.z3.Model
import com.microsoft.z3.Optimize
import com.microsoft.z3.Solver
import com.microsoft.z3.Sort
import com.microsoft.z3.Status
import java.math.BigInteger

abstract class Z3Scope(private val context: Context) {
    protected abstract val model: Model

    val Number.z3Int: IntExpr get() = context.mkInt(this.toLong())
    val String.z3Int: IntExpr get() = context.mkIntConst(this)
    fun Number.z3BV(bits: Int = 64): BitVecExpr = context.mkBV(this.toLong(), bits)
    fun String.z3BV(bits: Int = 64): BitVecExpr = context.mkBVConst(this, bits)
    val Expr<IntSort>.solution get() = (model.eval(this, true) as IntNum).int64
    val Expr<BitVecSort>.solution: BigInteger get() = (model.eval(this, true) as BitVecNum).bigInteger

    infix fun <T : Sort> Expr<T>.eq(other: Expr<T>): BoolExpr = context.mkEq(this, other)
    operator fun <T : ArithSort> Expr<T>.plus(other: Expr<T>): ArithExpr<T> = context.mkAdd(this, other)
    operator fun <T : ArithSort> Expr<T>.times(other: Expr<T>): ArithExpr<T> = context.mkMul(this, other)

    infix fun BitVecExpr.and(other: BitVecExpr): BitVecExpr = context.mkBVAND(this, other)
    infix fun BitVecExpr.xor(other: BitVecExpr): BitVecExpr = context.mkBVXOR(this, other)
    infix fun BitVecExpr.ushr(other: BitVecExpr): BitVecExpr = context.mkBVLSHR(this, other)
}

class OptimizeScope(private val impl: Optimize, context: Context) : Z3Scope(context) {
    override val model: Model get() = impl.model

    fun constraints(vararg conditions: Expr<BoolSort>) = impl.Assert(*conditions)

    fun <R : Sort> minimize(expr: Expr<R>) {
        impl.MkMinimize(expr)
    }

    fun solve() = impl.Check() == Status.SATISFIABLE
}

class SolverScope(private val impl: Solver, context: Context) : Z3Scope(context) {
    override val model: Model get() = impl.model

    fun constraints(vararg conditions: Expr<BoolSort>) = impl.add(*conditions)

    fun solve() = impl.check() == Status.SATISFIABLE
}

fun <T> z3Optimize(setup: OptimizeScope.() -> T): T {
    Context().use { ctx ->
        val scope = OptimizeScope(ctx.mkOptimize(), ctx)
        return scope.setup()
    }
}

fun <T> z3Solve(setup: SolverScope.() -> T): T {
    Context().use { ctx ->
        val scope = SolverScope(ctx.mkSolver(), ctx)
        return scope.setup()
    }
}