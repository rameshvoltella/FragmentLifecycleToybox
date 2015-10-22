package com.barryholroyd.fragment_lifecycle_toybox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Barry on 10/15/2015.
 */
public class MyFragmentStatic extends FragmentPrintStates
{
	InfoImpl  info  = new InfoImpl(this);
	private Trace trace = new Trace(Trace.LOGTAG_FRAGLC_STAT, Trace.SEP_FRAGMENT, info);
	//	DEL: private Trace tracePs = new Trace(Trace.LOGTAG_PRINT_STATE, Trace.SEP_PRINT_STATE, info);

	public MyFragmentStatic() {
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
		String tag = mfs.getTag();
		if (tag == null) tag = "<null>";
		String s = String.format(
			"Tag=%s getId=%#x C@HC=%s Retained=%b isAdded=%b ExplicitlyDetached=%b View=%s",
			tag, mfs.getId(), Trace.classAtHc(mfs), mfs.getRetainInstance(),
			mfs.isAdded(), mfs.isDetached(), Trace.classAtHc(mfs.getView())
		);
		return s;
	}
}
