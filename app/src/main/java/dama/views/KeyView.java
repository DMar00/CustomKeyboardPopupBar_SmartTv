package dama.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TableRow;
import androidx.annotation.Nullable;
import com.dama.customkeyboardpopupbarv2.R;
import dama.controllers.Controller;

public class KeyView extends ImageView {
    private float textSize;
    private int keyHeight;
    private int keyWidth;
    private String label;
    private Bitmap bitmap;
    private Drawable drawable;
    private String labelColor;


    public KeyView(Context context) {
        super(context);
    }

    public KeyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public KeyView(Context context, Drawable drawable, String label, String labelColor) {
        super(context);

        int height = (int) getResources().getDimension(R.dimen.key_height);
        int width = (int) getResources().getDimension(R.dimen.key_width);
        int textSize = (int) getResources().getDimension(R.dimen.key_text_size);

        //set drawable
        Drawable.ConstantState constantState = drawable.getConstantState();
        Drawable copiedDrawable = constantState.newDrawable();
        this.drawable = copiedDrawable;
        setBackground(this.drawable);

        //set dimension
        changeDimension(height, width, textSize);

        //set init label
        changeLabel(label, labelColor);
    }


    public void changeLabel(String label, String labelColor){
        this.label = label;
        this.labelColor = labelColor;

        setBitmap();

        if(this.label != null && this.label.length()>0){
            Canvas canvas = new Canvas(this.bitmap);
            Rect bounds = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
            Paint paint = createKeyLabel(labelColor);
            float textWidth = paint.measureText(this.label);
            float textHeight = paint.getTextSize();
            float x = bounds.centerX() - (textWidth / 2);
            float y = bounds.centerY() + (textHeight / 2);
            //setContentDescription(this.label);
            canvas.drawText(this.label, x, y, paint);
        }
    }

    public void changeDimension(int height, int width, int textSize){
        this.keyHeight = height;
        this.keyWidth = width;
        this.textSize = textSize;
        setBitmap();
        if(this.label != null && this.label.length()>0)
            changeLabel(this.label, this.labelColor);
    }

    public void addKeyParams(int code, boolean isSuggestion){
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        if(code == Controller.SPACE_KEY){
            params.span = 4; // key gets 4 cells
            changeDimension(this.keyHeight, ((this.keyWidth*4)+((4+4)*3)), 0);  //4+4(margin left/right)
        }
        if(isSuggestion){
            params.gravity = Gravity.CENTER;
            params.setMargins(0, 4, 0, 4);
        }else{
            params.setMargins(4, 4, 4, 4);
        }
        setLayoutParams(params);
    }

    public void changeDrawableColorStroke(int color){
        GradientDrawable gDrawable = (GradientDrawable) this.drawable.mutate();
        gDrawable.setStroke(3, color);
        //setBackground(gDrawable);
    }

    private void setBitmap(){
        this.bitmap = Bitmap.createBitmap(this.keyWidth, this.keyHeight ,Bitmap.Config.ARGB_8888);
        setImageBitmap(bitmap);
    }

    private Paint createKeyLabel(String color){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(this.textSize);
        paint.setAlpha(255);
        paint.setColor(Color.parseColor(color));
        paint.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        return paint;
    }

    public int getKeyHeight() {
        return keyHeight;
    }

    public int getKeyWidth() {
        return keyWidth;
    }
}
