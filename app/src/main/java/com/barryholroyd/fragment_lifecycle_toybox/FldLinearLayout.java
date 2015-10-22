package com.barryholroyd.fragment_lifecycle_toybox;

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
	private InfoImpl  info  = new InfoImpl(this);
	private Trace trace = new Trace(Trace.LOGTAG_VIEWGROUP, Trace.SEP_VIEWGROUP, info);
//	DEL: private Trace tracePs = new Trace(Trace.LOGTAG_PRINT_STATE, Trace.SEP_PRINT_STATE, info);

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
		trace.log("FldLinearLayout(NEW)");
		debug(5); // int is depth of indentation
	}
	//</editor-fold>
	//<editor-fold desc="OVERRIDES: VIEW GROUP">
	// API: 23
//	@Override
//	public void onViewAdded(View child) {
//		trace.log("onViewAdded");
//		super.onViewAdded(child);
//	}
	// API: 23
//	@Override
//	public void onViewRemoved(View child) {
//		trace.log("onViewRemoved");
//		super.onViewRemoved(child);
//	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (MainActivity.trace_layout_and_measure)
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
		if (MainActivity.trace_layout_and_measure)
			trace.log("onMeasure");
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
//	public void fldLlTrace() { // TBD:
////		DEL: tracePs.log("FldLinearLayout");
//		traceChildren();
//	}
//	private void traceChildren() { // TBD:
//		// Print container's children's info.
//		int child_cnt = getChildCount();
//		for (int i = 0 ; i < child_cnt ; i++) {
//			// There should only be FldTextView children present.
//			FldTextView fld_tv = (FldTextView) getChildAt(i);
//			fld_tv.fldTvTrace();
//		}
//	}
	// DEL: ?
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
				"FldLinearLayout: Id=%#x, C@HC=%s",
				fld_ll.getId(),  Trace.classAtHc(fld_ll));
			return s;
		}
	}
	//</editor-fold>
}
