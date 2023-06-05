package dama.views;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import com.dama.customkeyboardpopupbarv2.R;
import dama.utils.Utils;

public class PopupBarView extends GridLayout {
    private TextView redKeyLabel;
    private TextView greenKeyLabel;
    private TextView yellowKeyLabel;
    private TextView blueKeyLabel;


    public PopupBarView(Context context) {
        super(context);
    }

    public PopupBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PopupBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initialize(){
        redKeyLabel = findViewById(R.id.labelRedKey);
        greenKeyLabel = findViewById(R.id.labelGreenKey);
        yellowKeyLabel = findViewById(R.id.labelYellowKey);
        blueKeyLabel = findViewById(R.id.labelBlueKey);
        hide();
        //show();
    }

    public void setLabelKeys(String r, String g, String y, String b){
        redKeyLabel.setText(r);
        greenKeyLabel.setText(g);
        yellowKeyLabel.setText(y);
        blueKeyLabel.setText(b);
    }

    public void hide(){
        setVisibility(View.INVISIBLE);
    }

    public void show(){
        setVisibility(View.VISIBLE);
    }

    public void initPosition(CursorSpaceView cursorSpaceView, KeyView keyView){
        // Use post() to postpone code execution
        keyView.post(() -> {
            Rect offsetViewBounds = new Rect();
            keyView.getDrawingRect(offsetViewBounds);
            cursorSpaceView.offsetDescendantRectToMyCoords(keyView, offsetViewBounds);
            int x = offsetViewBounds.left;
            int y = offsetViewBounds.top;

            int lenPopupBar = getWidth();
            int lenKey = keyView.getWidth();

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.leftMargin = x - (lenPopupBar/2) + (lenKey/2); //set x
            layoutParams.topMargin = y - 25; //set y
            setLayoutParams(layoutParams);
        });
    }
}
