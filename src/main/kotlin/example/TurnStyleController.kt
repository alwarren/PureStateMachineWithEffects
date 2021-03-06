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

import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.delay
import example.Event.*
import example.Command.*

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "unused", "UnnecessaryVariable")
class TurnStyleController(
        val doorHardwareController: DoorHardwareController,
        val speakerController: SpeakerController,
        val stateMachine: TurnStyle
) {

    private val events = Channel<Event>(5)

    suspend fun consumeEvents() {
        for (event in events) {
            if (event == MachineDidFail) {
                askSomeoneToRepair()
            }
            val command = stateMachine.handleEvent(event)
            val nextEvent = handleCommand(command)
            if (nextEvent != null) events.send(nextEvent)
        }
        stateMachine.debug()
    }

    suspend fun shitHappens() {
        events.send(Event.MachineDidFail)
    }

    suspend fun askSomeoneToRepair() {
        delay(700)
        events.send(MachineRepairDidComplete)
    }


    suspend fun customerDidInsertCoin(value: Int) {
        events.send(InsertCoin(value))
    }


    suspend fun handleCommand(command: Command?): Event? {
        val nextEvent: Event? = when (command) {
            OpenDoors -> doorHardwareController.sendControlSignalToOpenDoors()
            SoundAlarm -> speakerController.soundTheAlarm()
            CloseDoors -> doorHardwareController.sendControlSignalToCloseDoors()
            null -> null
        }
        return nextEvent
    }

}