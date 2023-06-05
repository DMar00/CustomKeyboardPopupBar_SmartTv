package dama.views;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.dama.customkeyboardpopupbarv2.R;

public class CursorSpaceView extends FrameLayout {
    private KeyView cursor;
    private KeyView highlightRed;
    private KeyView highlightGreen;
    private KeyView highlightYellow;
    private KeyView highlightBlue;

    public CursorSpaceView(@NonNull Context context) {
        super(context);
    }

    public CursorSpaceView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CursorSpaceView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initCursorSpaceView(KeyView keyView){
        //init cursor
        Drawable cs = getResources().getDrawable(R.drawable.cursor);
        cursor = new KeyView(getContext(), cs, null, "#FBFBFB");
        initPosition(keyView, 0);
        //init highlight
        Drawable hl = getResources().getDrawable(R.drawable.suggestion_key);
        highlightRed = new KeyView(getContext(), hl, "", "#ffffff");
        highlightRed.changeDrawableColorStroke(ContextCompat.getColor(getContext(), R.color.red));
        highlightGreen = new KeyView(getContext(), hl, "", "#ffffff");
        highlightGreen.changeDrawableColorStroke(ContextCompat.getColor(getContext(), R.color.green));
        highlightYellow = new KeyView(getContext(), hl, "", "#ffffff");
        highlightYellow.changeDrawableColorStroke(ContextCompat.getColor(getContext(), R.color.yellow));
        highlightBlue = new KeyView(getContext(), hl, "", "#ffffff");
        highlightBlue.changeDrawableColorStroke(ContextCompat.getColor(getContext(), R.color.blue));
    }

    public void setPositionHighlights(KeyView r, KeyView g, KeyView yy, KeyView b){
        setPosition(highlightRed, r);
        setPosition(highlightGreen, g);
        setPosition(highlightYellow, yy);
        setPosition(highlightBlue, b);
    }

    public void showHighlights(){
        highlightRed.setVisibility(View.VISIBLE);
        highlightGreen.setVisibility(View.VISIBLE);
        highlightYellow.setVisibility(View.VISIBLE);
        highlightBlue.setVisibility(View.VISIBLE);
    }

    public void hideHighlights(){
        highlightRed.setVisibility(View.GONE);
        highlightGreen.setVisibility(View.GONE);
        highlightYellow.setVisibility(View.GONE);
        highlightBlue.setVisibility(View.GONE);
    }

    private void setPosition(KeyView in, KeyView out){
        // Use post() to postpone code execution
        out.post(() -> {
            removeView(in);
            Rect offsetViewBounds = new Rect();
            out.getDrawingRect(offsetViewBounds);
            offsetDescendantRectToMyCoords(out, offsetViewBounds);
            int x = offsetViewBounds.left;
            int y = offsetViewBounds.top;

            int lenPopupBar = getWidth();
            int lenKey = out.getWidth();

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.leftMargin = x; //set x
            layoutParams.topMargin = y; //set y
            in.setLayoutParams(layoutParams);
            in.changeDimension(out.getKeyHeight(), out.getKeyWidth(), 0);
            addView(in);
        });
    }

    public void initPosition(KeyView keyView, int k){
        KeyView cORh = cursor;

        keyView.post(() -> {
            Rect offsetViewBounds = new Rect();
            keyView.getDrawingRect(offsetViewBounds);

            offsetDescendantRectToMyCoords(keyView, offsetViewBounds);
            int x = offsetViewBounds.left;
            int y = offsetViewBounds.top;

            removeView(cORh);
            LayoutParams layoutParams = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
            );
            layoutParams.leftMargin = x; //set x
            layoutParams.topMargin = y; //set y
            cORh.setLayoutParams(layoutParams);
            addView(cORh);

            if(k==0)
                cORh.changeDimension(keyView.getKeyHeight(), keyView.getKeyWidth(), 0);
        });
    }

    public void moveCursor(KeyView keyView){
        //calculate destination coordinates
        Rect offsetViewBounds = new Rect();
        keyView.getDrawingRect(offsetViewBounds);
        offsetDescendantRectToMyCoords(keyView, offsetViewBounds);
        int x = offsetViewBounds.left;
        int y = offsetViewBounds.top;

        //create animation
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.ABSOLUTE, x - cursor.getX(),
                Animation.RELATIVE_TO_SELF, 0f, Animation.ABSOLUTE, y - cursor.getY()
        );
        animation.setDuration(350);

        //add listener
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //final view's position at the animation's end
                cursor.clearAnimation();
                LayoutParams layoutParams = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                );
                layoutParams.leftMargin = x;
                layoutParams.topMargin = y;
                cursor.setLayoutParams(layoutParams);

                cursor.changeDimension(keyView.getKeyHeight(), keyView.getKeyWidth(), 0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        //start animation on view
        cursor.startAnimation(animation);
    }
}
