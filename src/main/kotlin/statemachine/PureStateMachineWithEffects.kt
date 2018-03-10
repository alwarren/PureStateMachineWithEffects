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

import example.DoorHardwareController
import example.SpeakerController
import example.TurnStyle
import example.TurnStyleController
import kotlinx.coroutines.experimental.launch

infix fun <T> T?.shouldBe(expected: Any?) {
    if (this != expected) error("ShouldBe Failed!\nExpected: $expected\nGot:      $this")
}

/***
Now, an imperative shell that hides the enums and delegates to actuators.
Note that it has no domain knowledge: it just connects object interfaces.
***/

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
suspend fun runStateMachineWithSideEffects(): TurnStyleController {
    val controller = TurnStyleController(DoorHardwareController(), SpeakerController(), TurnStyle())
    launch { controller.consumeEvents() }
    return controller
}