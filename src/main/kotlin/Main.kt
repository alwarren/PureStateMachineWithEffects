import example.*
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import statemachine.runStateMachineWithSideEffects
import statemachine.shouldBe

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
fun main(args: Array<String>) {

    /*** The functional core of the state machine is suepr trivial to test **/

    val events: List<Event> = listOf(
            Event.InsertCoin(20), Event.InsertCoin(20), Event.InsertCoin(10),
            Event.AdmitPerson,
            Event.InsertCoin(1),
            Event.MachineDidFail,
            Event.MachineRepairDidComplete)

    val expectedStates = listOf(
            State.Locked(0), State.Locked(20), State.Locked(40), State.Unlocked,
            State.Locked(0), State.Locked(1), State.Broken(State.Locked(1)), State.Locked(0)
    )
    val expectedCommands: List<Command?> = listOf(null, null, null, Command.OpenDoors, Command.CloseDoors, null, null, null)

    val stateMachine = TurnStyle()
    events.forEach { e -> stateMachine.handleEvent(e) }

    stateMachine.debug()

    stateMachine.statesHistory() shouldBe expectedStates
    stateMachine.commandHistory() shouldBe expectedCommands


    /** The imperative shell takes care of the side Effects **/

    runBlocking {
        val controller = runStateMachineWithSideEffects()

        controller.customerDidInsertCoin(10)
        delay(100)
        controller.customerDidInsertCoin(50)
        delay(100)
//        controller.shitHappens()
        delay(2000)
        controller.stateMachine.debug()
        controller.doorHardwareController.msgs shouldBe listOf("sendControlSignalToOpenDoors", "sendControlSignalToCloseDoors")

    }

}
