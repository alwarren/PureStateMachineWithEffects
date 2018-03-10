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

import statemachine.StateMachine
import example.Command.*
import example.State.*
import example.Event.*

/***
 * Functional Core of our state machine.
 */
class TurnStyle : StateMachine<State, Event, Command> {

    override fun initialState(): State = State.Locked(credit = 0)

    override fun currentState(): State = history.last().first

    private val history = mutableListOf(initialState() to doNothing)

    private val events = mutableListOf<Event>()

    override fun statesHistory(): List<State> = history.map { it.first }

    override fun commandHistory(): List<Command?> = history.map { it.second }

    override fun eventsHistory(): List<Event> = events.toList()

    override fun handleEvent(event: Event): Command? {
        events += event
        val currentState = currentState()

        val nextMove: Pair<State, Command?>? = when (currentState) {
            is Locked -> when (event) {
                is Event.InsertCoin -> {
                    val newCredit = currentState.credit + event.value
                    if (newCredit >= FARE_PRICE)
                        Unlocked.emit(OpenDoors)
                    else
                        Locked(newCredit).move()
                }
                AdmitPerson -> currentState.emit(SoundAlarm)
                MachineDidFail -> Broken(oldState = currentState).move()
                MachineRepairDidComplete -> null
            }
            Unlocked -> when (event) {
                AdmitPerson -> Locked(credit = 0).emit(CloseDoors)
                else -> null
            }
            is Broken -> when (event) {
                MachineRepairDidComplete -> Locked(credit = 0).move()
                else -> null
            }
        }

        if (nextMove == null) {
            fail("Unexpected event $event from state $currentState")
        } else {
            history.add(nextMove)
            return nextMove.second
        }

        return null
    }

    private fun fail(message: String) = println(message)

    companion object {
        private val doNothing: Command? = null
        const val FARE_PRICE = 50
    }
}