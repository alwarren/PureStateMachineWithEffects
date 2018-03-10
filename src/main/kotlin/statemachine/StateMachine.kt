package statemachine

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

import statemachine.StateMachine.*

/** Generic State Machine **/


interface StateMachine<State : StateType, Event : StateEvent, Command : StateCommand> {
    interface StateEvent
    interface StateType
    interface StateCommand

    fun initialState(): State

    fun currentState(): State

    fun handleEvent(event: Event): Command?

    fun statesHistory(): List<State>

    fun commandHistory(): List<Command?>

    fun eventsHistory(): List<Event>

    // utility functions to model a transition with or without an emitted command
    fun State.move(): Pair<State, Command?> = Pair(this, null)

    fun State.emit(command: Command?): Pair<State, Command?> = Pair(this, command)

    fun debug() {

        println("""
Events:   ${printList(eventsHistory())}
States:   ${printList(statesHistory())}
Commands: ${printList(commandHistory())}
    """)
    }

    private fun printList(list: List<Any?>) = list.joinToString(prefix = "listOf(", postfix = ")")
}



