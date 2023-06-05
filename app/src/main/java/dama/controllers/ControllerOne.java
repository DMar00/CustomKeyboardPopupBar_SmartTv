package dama.controllers;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import dama.views.CursorSpaceView;
import dama.views.KeyboardView;
import dama.views.PopupBarView;

public class ControllerOne extends Controller{
    public ControllerOne(Context ctx, Keyboard keyboard, CursorSpaceView cursorSpaceView, KeyboardView keyboardView, PopupBarView popupBarView) {
        super(ctx, keyboard, cursorSpaceView, keyboardView, popupBarView);
    }
}
