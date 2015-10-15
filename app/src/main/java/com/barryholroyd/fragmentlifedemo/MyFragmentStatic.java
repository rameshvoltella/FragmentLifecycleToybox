package com.barryholroyd.fragmentlifedemo;

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
	private Trace trace = null;

	public MyFragmentStatic() {
		super.setLogTag(Trace.LOGTAG_FRAGLC_STAT);
		trace = new Trace(Trace.LOGTAG_FRAG_STAT, Trace.SEP_FRAGMENT, null);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// For static fragments, the container is always null at this point (verify).
		TextView tv = (TextView) inflater.inflate(R.layout.static_textview_to_inflate, null, false);
		String s = String.format("Inflated static_textview_to_inflate [%s]", Trace.getIdHc(tv));
		trace.log("onCreateView()", "New: " + s);

		return tv;
	}
}
