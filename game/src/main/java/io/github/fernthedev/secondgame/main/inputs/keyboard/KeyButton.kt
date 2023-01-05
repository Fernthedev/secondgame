package io.github.fernthedev.secondgame.main.inputs.keyboard


class KeyButton(
    val charCode: Int = 0,
    var held: Boolean = false
) {

    private var pressed = 0

    fun isPressed(): Boolean {
        if (held && pressed != 0) {
            pressed--
            return true
        }
        return false
    }

}