package example

/***
 * See https://gist.github.com/jmfayard/ac6a94df1cc2994ab5b59f510c98133f#file-purestatemachinewitheffects-kt
 * Original content follows
 *
 * Context: I highly recommend andymatuschak's  gist
 *
 * A composable pattern for pure state machines with effects
 * https://gist.github.com/andymatuschak/d5f0a8730ad601bcccae97e8398e25b2
 *
 * It's written in swift but nicely maps to Kotlin as demonstrated here
 *
 * See the schema of the TurnStyle here
 *
 * ![TurnStyle](https://camo.githubusercontent.com/a74ea94a7eab348f991fb22d6f70a92c5bef3740/68747470733a2f2f616e64796d617475736368616b2e6f72672f7374617465732f666967757265332e706e67)
 ***/

import statemachine.StateMachine.StateType

sealed class State(private val msg: String? = null) : StateType {
    data class Locked(val credit: Int) : State()
    object Unlocked : State("Unlocked")
    data class Broken(val oldState: State) : State()

    override fun toString(): String =
            msg ?: super.toString()

}