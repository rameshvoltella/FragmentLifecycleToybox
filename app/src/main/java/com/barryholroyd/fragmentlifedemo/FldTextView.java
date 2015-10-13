package com.barryholroyd.fragmentlifedemo;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by Barry on 10/12/2015.
 */

/*
 * Use these?
 *   getParent()
 *   getRootView()
 */
public class FldTextView extends TextView
{
	static public Bhlogger bh = new Bhlogger("FLD VGP");
	private int id = 0;

	public FldTextView(Context context) {
		super(context);
		id = generateViewId();
		setId(id);

		// setTag(key, Object);

		trace("FldTextView");
	}

//	public FldTextView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//	}
//
//	public FldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
//		super(context, attrs, defStyleAttr);
//	}
//
//	public FldTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//		super(context, attrs, defStyleAttr, defStyleRes);
//	}

	@Override
	protected void onAttachedToWindow() {
		// This is called when the view is attached to a window.
		// At this point it has a Surface and will start drawing.
		trace("onAttachedToWindow");
		super.onAttachedToWindow();
	}
	@Override
	public void onDetachedFromWindow() {
		trace("onDetachedFromWindow");
		super.onDetachedFromWindow();
	}
	@Override
	protected void onFinishInflate() {
		trace("onFinishInflate");
		super.onFinishInflate();
	}
//	@Override
//	public void onLayout(boolean changed, int left, int top, int right, int bottom) {
//		// TBD: is this o.k.?
//		super.onLayout(changed, left, top, right, bottom);
//		trace("onLayout");
//	}
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		// TBD: is this o.k.? I'm not calling setMeasuredDimension().
//		trace("onMeasure");
//	}
//	@Override
//	public Parcelable onSaveInstanceState() {
//		trace("onSaveInstanceState");
//		return super.onSaveInstanceState();
//	}
//	@Override
//	public void onRestoreInstanceState(Parcelable state) {
//		super.onRestoreInstanceState(state);
//		trace("onRestoreInstanceState");
//	}

	private void trace(String label) { trace(label, ""); }
	private void trace(String label, String msg) {
		String s = String.format("%s%s", MainActivity.SEP_VIEW,getFldTextViewInfo(label, msg));
		bh.log(s);
	}
	public String getFldTextViewInfo(String label, String msg) {
		String s = String.format(
			"%-15s [%#x] (%s): %s",
			label, this.getId(), this.toString(), msg);
		return s;
	}
}
