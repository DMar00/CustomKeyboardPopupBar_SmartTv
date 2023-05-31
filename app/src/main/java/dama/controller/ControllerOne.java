package dama.controller;

import android.inputmethodservice.Keyboard;
import dama.views.CursorSpaceView;
import dama.views.KeyboardView;

public class ControllerOne extends Controller{
    public ControllerOne(Keyboard keyboard, CursorSpaceView cursorSpaceView, KeyboardView keyboardView) {
        super(keyboard, cursorSpaceView, keyboardView);
    }
}
