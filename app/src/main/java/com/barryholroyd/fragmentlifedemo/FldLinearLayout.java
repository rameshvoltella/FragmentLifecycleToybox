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
	private Bhlogger bh = new Bhlogger("FLD  VW");
	private int id = 0;

	public FldLinearLayout(Context context) {
		super(context);
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
		String s = String.format("%s%s", MainActivity.SEP_VGRP,getFldLinearLayoutInfo(label, msg));
		bh.log(s);
	}
	public String getFldLinearLayoutInfo(String label, String msg) {
		String s = String.format(
			"%-15s [%#x] (%s): %s",
			label, this.getId(), this.toString(), msg);
		return s;
	}

}
