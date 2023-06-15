package dama.controllers;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.util.Log;
import android.view.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dama.utils.Cell;
import dama.utils.Key;
import dama.utils.Utils;
import dama.views.CursorSpaceView;
import dama.views.KeyboardView;
import dama.views.PopupBarView;

public abstract class Controller {
    public static final int COLS = 10;
    public static final int ROWS = 5;
    public static final int MAX_SUG = 4;
    public static final int INVALID_KEY = -1;
    public static final int HIDDEN_KEY = -3;
    public static final int SPACE_KEY = 32;

    private Cell focus;
    private Cell prevFocus;
    private boolean barsShown;
    private SuggestionsController suggestionsController;

    private HashMap<Integer, ArrayList<Key>> keys;
    private HashMap<Character, Cell> letterPositions;

    private CursorSpaceView cursorSpaceView;
    private KeyboardView keyboardView;
    private PopupBarView popupBarView;
    private ArrayList<Key> barKeys;

    /*********************************INIT*************************************/
    public Controller(Context ctx, Keyboard keyboard, CursorSpaceView cursorSpaceView, KeyboardView keyboardView, PopupBarView popupBarView) {
        this.keys = new HashMap<>();
        this.letterPositions = new HashMap<>();
        this.barKeys = new ArrayList<>();

        this.focus = new Cell(1,0);     //first focus on Q
        this.prevFocus = new Cell(0,0);

        this.barsShown = false;
        this.suggestionsController = new SuggestionsController(ctx);

        this.cursorSpaceView = cursorSpaceView;
        this.keyboardView = keyboardView;
        this.popupBarView = popupBarView;

        loadKeys(keyboard);
        drawKeyboard();
    }

    private void loadKeys(Keyboard keyboard){
        if(keys.size()==0){
            int cols = COLS;
            for(int i=0 ; i<ROWS; i++) {
                //todo problem with other keys in ROWS-1
                if(i==ROWS-1) cols = 7;
                ArrayList<Key> rowKeys = new ArrayList<>();
                for (int j = 0; j < cols; j++) {
                    Keyboard.Key k = keyboard.getKeys().get(getKeyIndex(new Cell(i, j)));
                    Key key = new Key(k.codes[0], k.label.toString(), k.icon);  //todo MOOD k.icon instead null
                    if(key.getLabel().length()==1) //todo instead of >0
                        letterPositions.put(key.getLabel().charAt(0), new Cell(i, j));
                    rowKeys.add(key);
                }
                this.keys.put(i, rowKeys);
            }
        }

    printMap(letterPositions);
    }

    private  void printMap(HashMap<Character, Cell> hashMap){
        for (Map.Entry<Character, Cell> entry : hashMap.entrySet()) {
            Character chiave = entry.getKey();
            Cell valore = entry.getValue();
            Log.d("printMap",chiave + " -> " + valore);
        }
    }

    private int getKeyIndex(Cell cell){
        return ((cell.getRow()*this.COLS))+cell.getCol();
    }

    private void drawKeyboard(){
        this.keyboardView.initKeyboardView(keys);
        this.cursorSpaceView.initCursorSpaceView(this.keyboardView.getKeyViewAtCell(focus));

        this.popupBarView.initialize();
        this.cursorSpaceView.addView(popupBarView);
        this.popupBarView.initPosition(cursorSpaceView, keyboardView.getKeyView(getFocus()));
    }

    /*********************************SUGGESTION CONTROLLER*************************************/
    public SuggestionsController getSuggestionsController() {
        return suggestionsController;
    }

    /*********************************FOCUS*************************************/
    public Cell newFocus(int direction){
        Cell newCell = new Cell(0,0);
        switch (direction){
            case KeyEvent.KEYCODE_DPAD_LEFT:
                newCell.setRow(this.focus.getRow());
                newCell.setCol(this.focus.getCol()-1);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                newCell.setRow(this.focus.getRow());
                newCell.setCol(this.focus.getCol()+1);
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                int r1 = this.focus.getRow() - 1;
                int c1 = this.focus.getCol();
                if(this.focus.getRow() == (ROWS-1)){
                    Log.d("up at last row", "col: "+this.focus.getCol());
                    switch (this.focus.getCol()){
                        case 4:
                            c1 = 7;
                            break;
                        case 5:
                            c1 = 8;
                            break;
                        case 6:
                            c1 = 9;
                            break;
                    }
                }
                newCell.setRow(r1);
                newCell.setCol(c1);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                int r2 = this.focus.getRow() + 1;
                int c2 = this.focus.getCol();
                //if i have to go to space bar
                if(this.focus.getRow() == (ROWS-2)  && (this.focus.getCol()>=4 && this.focus.getCol()<=6)) {
                    if(!isBarsShown())
                        c2 = 3; //in col 3 row 4 there is space bar
                }else if(this.focus.getRow() == (ROWS-2)  && (this.focus.getCol()>=7 && this.focus.getCol()<=9)){
                    //todo i have adding this if
                    switch (this.focus.getCol()){
                        case 7:
                            c2 = 4;
                            break;
                        case 8:
                            c2 = 5;
                            break;
                        case 9:
                            c2 = 6;
                            break;
                    }
                }
                newCell.setCol(c2);
                newCell.setRow(r2);
                break;
        }
        return newCell;
    }

    public boolean isNextFocusable(Cell newFocus, int direction){
        if(isFocusInRange(newFocus) && !(isInvalidKey(newFocus)) && !(isHiddenKey(newFocus))){
            return true;
            //todo comment this
            /*if(direction == KeyEvent.KEYCODE_DPAD_LEFT || direction == KeyEvent.KEYCODE_DPAD_RIGHT){
                return focus.getRow() == newFocus.getRow();

            }else if (direction == KeyEvent.KEYCODE_DPAD_UP || direction == KeyEvent.KEYCODE_DPAD_DOWN) {
                if(newFocus.getRow()!= (ROWS-1))    //for space key not same column
                    return focus.getCol() == newFocus.getCol();
                else return true;

            }*/
        }
        return false;
    }

    public boolean isFocusInRange(Cell focus) {
        //todo in the last row if i go over horizontal 7th key
        int cols = COLS;
        if(focus.getRow() == ROWS-1)
            cols = 7;
        return (focus.getCol() < cols && focus.getRow() < ROWS && focus.isValidPosition());
    }

    public boolean isHiddenKey(Cell focus){
        Key key = null;
        if(focus.isValidPosition() && isFocusInRange(focus))
            key = keys.get(focus.getRow()).get(focus.getCol());
        return (key == null || key.getCode() == HIDDEN_KEY);
    }

    private boolean isInvalidKey(Cell focus){
        Key key = keys.get(focus.getRow()).get(focus.getCol());
        return (key == null || key.getCode() == INVALID_KEY);
    }

    public Cell getFocus() {
        return focus;
    }

    public void setFocus(Cell newFocus){
        this.focus = newFocus;
    }

    public void moveFocusPosition(Cell newFocus){
            this.cursorSpaceView.moveCursor(this.keyboardView.getKeyViewAtCell(newFocus));
    }

    /*********************************SUGGESTIONS*************************************/
    public void showPopUpBar(String ctx){
        //generate suggestions checking letters pressed
        char[] suggestions = getSuggestions(ctx);
        Log.d("showPopUpBar",suggestions.length+"");
        //fill popUpBar
        int suggestionsLength = suggestions.length;
        barKeys.clear();
        for(char c:suggestions){
            this.barKeys.add(charToKey(c));
        }
        //TODO change _ to space symbol
        if(suggestionsLength == 4){
            String red;
            if(suggestions[0]==' ')
                red = "_";
            else red = suggestions[0]+"";

            String green;
            if(suggestions[1]==' ')
                green = "_";
            else green = suggestions[1]+"";

            String yellow;
            if(suggestions[2]==' ')
                yellow = "_";
            else yellow = suggestions[2]+"";

            String blue;
            if(suggestions[3]==' ')
                blue = "_";
            else blue = suggestions[3]+"";

            popupBarView.setLabelKeys(red, green, yellow, blue);
        }

        //showPopupBar
        popupBarView.initPosition(this.cursorSpaceView, keyboardView.getKeyView(getFocus()));
        popupBarView.show();

        //showHighlights
        Cell rCell = letterPositions.get(suggestions[0]);
        Cell gCell = letterPositions.get(suggestions[1]);
        Cell yCell = letterPositions.get(suggestions[2]);
        Cell bCell = letterPositions.get(suggestions[3]);
        Log.d("cells highlights",""+rCell+" - "+gCell+" - "+yCell+" - "+bCell);
        cursorSpaceView.setPositionHighlights(keyboardView.getKeyView(rCell), keyboardView.getKeyView(gCell), keyboardView.getKeyView(yCell), keyboardView.getKeyView(bCell));
        cursorSpaceView.showHighlights();

        barsShown = true;
    }

    public void hidePopUpBar(){
        popupBarView.hide();
        cursorSpaceView.hideHighlights();
        barKeys.clear();
        barsShown = false;
    }

    public void pressColorKey(int i, String ctx){
        //get key in color highlight
        Key coloredKey = barKeys.get(i);
        Cell newFocus = letterPositions.get(coloredKey.getLabel().charAt(0));
        //
        hidePopUpBar();

        //set new focus
        setFocus(newFocus);
        moveFocusPosition(getFocus());

        //show bar on new focus
        showPopUpBar(ctx);
    }


    protected char[] getSuggestions(String ctx) {
        //char prevChar = getKeyAtPos(getPrevFocus().getRow(), getPrevFocus().getCol()).getLabel().charAt(0);
        char focusChar = getKeyAtPos(getFocus().getRow(), getFocus().getCol()).getLabel().charAt(0);
        char ch[] = getSuggestionsController().generateSuggestions(ctx);

        char[] suggestions = new char[MAX_SUG];
        for(int i=0, j=0; i<ch.length && j<MAX_SUG; i++){
            if(ch[i] != focusChar){
                suggestions[j] = ch[i];
                j++;
            }
        }
        return suggestions;

    }

    public boolean isBarsShown() {
        return barsShown;
    }

    public void setBarsShown(boolean barsShown) {
        this.barsShown = barsShown;
    }

    public ArrayList<Key> getBarKeys() {
        return barKeys;
    }

    /*********************************KEYS*************************************/
    public ArrayList<Key> getKeysAtRow(int index){
        return keys.get(index);
    }

    public Key getKeyAtPos(int row, int col){
        return getKeysAtRow(row).get(col);
    }

    public Key charToKey(char c){
        Cell cell = letterPositions.get(c);
        Log.d("charToKey", "cell: "+cell+" - char: "+c);
        Key key = getKeyAtPos(cell.getRow(), cell.getCol()).clone();
        return key;
    }

    protected void modifyKey(Cell cell, String label, int code){
        getKeyAtPos(cell.getRow(),cell.getCol()).setLabel(label);
        getKeyAtPos(cell.getRow(),cell.getCol()).setCode(code);
        getKeyboardView().getKeyView(cell).changeLabel(label,"#FBFBFB");
    }

    /*********************************KEYBOARD VIEW*************************************/
    public KeyboardView getKeyboardView() {
        return keyboardView;
    }

    public CursorSpaceView getCursorSpaceView() {
        return cursorSpaceView;
    }
}
