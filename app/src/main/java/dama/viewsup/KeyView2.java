package dama.viewsup;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dama.customkeyboardpopupbarv2.R;

public class KeyView2 extends RelativeLayout {
    public final static int SPACE = 3;
    private ImageView backgroundImage;
    private TextView label;
    private ImageView icon;
    private int colSize;
    private int kHeight;
    private int kWidth;
    private int kTextSize;

    public KeyView2(Context context) {
        super(context);
        initKeyView();
    }

    public KeyView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initKeyView();
    }

    public KeyView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initKeyView();
    }

    private void initKeyView() {
        //set default size
        kHeight = (int) getResources().getDimension(R.dimen.key_height);
        kWidth = (int) getResources().getDimension(R.dimen.key_width);
        kTextSize = (int) getResources().getDimension(R.dimen.key_text_size);
        setDimens(kHeight, kWidth);

        //set default cols
        colSize = 1;

        //set default icon
        icon = null;

        //set default label
        label = null;

        //set default background
        initBackground();
        addView(backgroundImage);
    }

    //getters
    public int getKeyHeight() {
        return kHeight;
    }

    public int getKeyWidth() {
        return kWidth;
    }

    //setters
    public void setDimens(int height, int width){
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        setLayoutParams(layoutParams);
        requestLayout();

        kHeight = height;
        kWidth = width;
    }

    public void setLabel(String text){
        if(text!=null && text.length()>0){
            if(label == null){
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                label = new TextView(getContext());
                label.setTextSize(TypedValue.COMPLEX_UNIT_SP, kTextSize);
                addView(label, layoutParams);
            }
            label.setText(text);
        }
    }

    public void setIcon(Drawable drawable){
        if(icon==null){
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            ImageView imageView = new ImageView(getContext());
            addView(imageView, layoutParams);
        }
        //icon.setImageResource(R.drawable.your_image);
        icon.setImageDrawable(drawable);
    }

    private void setColorLabel(int color){
        label.setTextColor(color);
    }

    public void setBackgroundImage(Drawable drawable){
        backgroundImage.setImageDrawable(drawable);
    }

    public void setBackgroundColorImage(int color){
        backgroundImage.setBackgroundColor(color);
    }

    public void setColSize(int cols){
        int w = (cols * kWidth) + ((cols-1)*SPACE);
        colSize = cols;
        kWidth = w;
        setDimens(kHeight, kWidth);
    }

    /*init methods*/
    private void initBackground(){
        backgroundImage = new ImageView(getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        backgroundImage.setLayoutParams(layoutParams);
        backgroundImage.requestLayout();
    }



}
