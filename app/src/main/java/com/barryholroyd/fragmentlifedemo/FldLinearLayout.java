package com.barryholroyd.fragmentlifedemo;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Barry on 10/12/2015.
 */
public class FldLinearLayout extends LinearLayout
{
	private Bhlogger bh = new Bhlogger("FLD  LAY");
	private int id = 0;

	public FldLinearLayout(Context context) {
		super(context);
		init();
	}

	public FldLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public FldLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public FldLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int
		defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	private void init() {
		id = getId();
		debug(5); // int is depth of indentation
	}

	//  ------------------------------------------------------------------
	//  From ViewGroup
	//  ------------------------------------------------------------------
	@Override
	public void onViewAdded(View child) {
		super.onViewAdded(child);
		trace("onViewAdded");
	}

	@Override
	public void onViewRemoved(View child) {
		super.onViewRemoved(child);
		trace("onViewRemoved");
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		trace("onLayout");
	}

	//  ------------------------------------------------------------------
	//  From View
	//  ------------------------------------------------------------------
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
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// TBD: is this o.k.? I'm not calling setMeasuredDimension().
		trace("onMeasure");
	}
	@Override
	public Parcelable onSaveInstanceState() {
		trace("onSaveInstanceState");
		return super.onSaveInstanceState();
	}
	@Override
	public void onRestoreInstanceState(Parcelable state) {
		super.onRestoreInstanceState(state);
		trace("onRestoreInstanceState");
	}

	//  ------------------------------------------------------------------
	//  Support Methods
	//  ------------------------------------------------------------------
	private void trace(String label) { trace(label, ""); }

	private void trace(String label, String msg) {
		String data = getFldLinearLayoutInfo();
		MainActivity.traceMain(MainActivity.SEP_VIEW, label, data, msg);
	}
	public String getFldLinearLayoutInfo() {
		String s = String.format("ThisId=%#x, This:%-22s",
			this.getId(), this.toStringSimple());
		return s;
	}

	public String toStringSimple() {
		return getClass().getSimpleName() + '@' + Integer.toHexString(hashCode());
	}

}
