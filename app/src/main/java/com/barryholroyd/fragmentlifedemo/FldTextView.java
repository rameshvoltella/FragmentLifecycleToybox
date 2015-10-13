package com.barryholroyd.fragmentlifedemo;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by Barry on 10/12/2015.
 */

public class FldTextView extends TextView
{
	//<editor-fold desc="FIELDS">
	private Trace trace = null;
	//</editor-fold>
	//<editor-fold desc="CONSTRUCTORS">
	public FldTextView(Context context) {
		super(context);
		setId(generateViewId());
		InfoImpl  info  = new InfoImpl(this);
		trace = new Trace("FLD VEW", Trace.SEP_VEW, info);
		trace.log("FldTextView(NEW)");
	}
	//</editor-fold>
	//<editor-fold desc="OVERRIDES">
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
//	@Override
//	public void onLayout(boolean changed, int left, int top, int right, int bottom) {
//		// TBD: is this o.k.?
//		super.onLayout(changed, left, top, right, bottom);
//		trace.log("onLayout");
//	}
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		// TBD: is this o.k.? I'm not calling setMeasuredDimension().
//		trace.log("onMeasure");
//	}
//	@Override
//	public Parcelable onSaveInstanceState() {
//		trace.log("onSaveInstanceState");
//		return super.onSaveInstanceState();
//	}
//	@Override
//	public void onRestoreInstanceState(Parcelable state) {
//		super.onRestoreInstanceState(state);
//		trace.log("onRestoreInstanceState");
//	}
	//</editor-fold>
	//<editor-fold desc="TRACE SUPPORT">
	public void fldTvTrace() {
		trace.log("FldTextView");
	}
	class InfoImpl implements Trace.Info
	{
		// "this", from the object that created this instance
		private Object obj = null;
		InfoImpl(Object obj) {
			this.obj = obj;
		}
		public String getData() {
			FldTextView fld_tv = (FldTextView) obj;
			String s = String.format(
				"TextViewId=%#x, TextViewHashCode=%#x, TextView2String=%s",
				fld_tv.getId(), fld_tv.hashCode(), Trace.toStringSimple(fld_tv));
			return s;
		}
	}
	//</editor-fold>
}
