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
	//<editor-fold desc="FIELDS">
	private Trace trace = null;
	//</editor-fold>
	//<editor-fold desc="CONSTRUCTORS">
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
	private void init() {
		InfoImpl  info  = new InfoImpl(this);
		trace = new Trace(MainActivity.LOGTAG_VIEWGROUP, Trace.SEP_VGP, info);
		debug(5); // int is depth of indentation
		trace.log("FldLinearLayout(NEW)");
	}
	//</editor-fold>
	//<editor-fold desc="OVERRIDES: VIEW GROUP">
	@Override
	public void onViewAdded(View child) {
		trace.log("onViewAdded");
		super.onViewAdded(child);
	}
	@Override
	public void onViewRemoved(View child) {
		trace.log("onViewRemoved");
		super.onViewRemoved(child);
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		trace.log("onLayout");
		super.onLayout(changed, l, t, r, b);
	}
	//</editor-fold>
	//<editor-fold desc="OVERRIDES: VIEW    ">
	@Override
	protected void onAttachedToWindow() {
		// This is called when the view is attached to a window.
		// At this point it has a Surface and will start drawing.
		trace.log("onAttachedToWindow");
		super.onAttachedToWindow();
	}
	@Override
	public void onDetachedFromWindow() {
		trace.log("onDetachedFromWindow");
		super.onDetachedFromWindow();
	}
	@Override
	protected void onFinishInflate() {
		trace.log("onFinishInflate");
		super.onFinishInflate();
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		trace.log("onMeasure");
		// TBD: is this o.k.? I'm not calling setMeasuredDimension().
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	@Override
	public Parcelable onSaveInstanceState() {
		trace.log("onSaveInstanceState");
		return super.onSaveInstanceState();
	}
	@Override
	public void onRestoreInstanceState(Parcelable state) {
		trace.log("onRestoreInstanceState");
		super.onRestoreInstanceState(state);
	}
	//</editor-fold>
	//<editor-fold desc="TRACE SUPPORT">
	public void fldLlTrace() {
		trace.log("FldLinearLayout");
		traceChildren();
	}
	private void traceChildren() {
		// Print container's children's info.
		int child_cnt = getChildCount();
		for (int i = 0 ; i < child_cnt ; i++) {
			// There should only be FldTextView children present.
			FldTextView fld_tv = (FldTextView) getChildAt(i);
			fld_tv.fldTvTrace();
		}
	}
	class InfoImpl implements Trace.Info
	{
		// "this", from the object that created this instance
		private Object obj = null;
		InfoImpl(Object obj) {
			this.obj = obj;
		}
		public String getData() {
			FldLinearLayout fld_ll = (FldLinearLayout) obj;
			String s = String.format(
				"ContainerId=%#x, ContainerHashCode=%#x, Container2String=%s",
				fld_ll.getId(), fld_ll.hashCode(), Trace.toStringSimple(fld_ll));
			return s;
		}
	}
	//</editor-fold>
}
