package com.barryholroyd.fragmentlifedemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Barry on 10/15/2015.
 */
public class MyFragmentStatic extends FragmentPrintStates
{
	private Trace trace = null;

	public MyFragmentStatic() {
		InfoImpl  info  = new InfoImpl(this);
		trace = new Trace(Trace.LOGTAG_FRAGLC_STAT, Trace.SEP_FRAGMENT, info);
//	DEL:	super.setLogTag(Trace.LOGTAG_FRAGLC_STAT);
		trace.log("MyFragmentStatic()", String.format("MyFragmentStatic(NEW)=%s",
			Trace.classAtHc(this)));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// For static fragments, the container is always null at this point (verify).
		TextView statictv = (TextView) inflater.inflate(R.layout.static_textview, null, false);
		trace.log("onCreateView()", String.format("MyFragmentStatic: statictv=%s, container=%s",
			Trace.classAtHc(statictv), Trace.classAtHc(container)));
		return statictv;
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
		MyFragmentStatic mfs = (MyFragmentStatic) obj;
		// Object	host_obj	= mf.getHost(); // Requires API 23
		FldTextView fldtv = (FldTextView) mfs.getView();
		String fldtv_class_at_hc = fldtv == null ? "<null>" : Trace.classAtHc(fldtv);
		String tag = mfs.getTag();
		if (tag == null) tag = "<null>";
		String s = String.format(
			"getId()=%#x C@HC=%s Tag=%s Added=%b Attached=%b View=%s",
			mfs.getId(), Trace.classAtHc(mfs),
			tag, mfs.isAdded(), !mfs.isDetached(), fldtv_class_at_hc
		);
		return s;
	}
}
