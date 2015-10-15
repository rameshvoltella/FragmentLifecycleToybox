package com.barryholroyd.fragmentlifedemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by Barry on 10/12/2015.
 */
public class MyFragment extends FragmentPrintStates
{
	static final private String ARG_FTAG            = "ARG_FTAG";
	static final private String ARG_CONTAINER_RID   = "ARG_CONTAINER_RID";

	private Trace   trace           = null;
	private String  ftag            = "<null>";// Needed until tag is added in a transaction.
	private int     container_rid   = 0;

	public void init(String ftag, int container_rid) {
		this.ftag = ftag;
		this.container_rid = container_rid;
	}

	public MyFragment() {
		InfoImpl  info  = new InfoImpl(this);
		trace = new Trace(Trace.LOGTAG_FRAG_DYN, Trace.SEP_FRAGMENT, info);
		super.setLogTag(Trace.LOGTAG_FRAGLC_DYN);
		trace.log("MyFragment()", String.format("MyFragment(NEW): %s", Trace.getIdHc(this)));
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			ftag            = savedInstanceState.getString(ARG_FTAG);
			container_rid   = savedInstanceState.getInt(ARG_CONTAINER_RID);
		}
	}

	public String getMyTag() { return ftag; } // distinct from Fragment.getTag()
	public int getContainerId() { return container_rid; }
	public void trace() {
		trace.log("MyFragment");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		trace.log("onCreateView()",
			String.format("New fragment: container_rid=%s",
				container == null ? "*** <NULL> ***" : Trace.getIdHc(container)));

		FrameLayout fl = (FrameLayout) inflater.inflate(
			R.layout.fld_text_view_layout, null, false);

		return fl;
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		trace.log("onSaveInstanceState()");
		super.onSaveInstanceState(outState);
		outState.putString(ARG_FTAG, ftag);
		outState.putInt(ARG_CONTAINER_RID, container_rid);
	}

	class InfoImpl implements Trace.Info
	{
		// "this", from the object that created this instance
		private Object obj = null;

		InfoImpl(Object obj) {
			this.obj = obj;
		}

		public String getData() {
			MyFragment mf = (MyFragment) obj;
			boolean is_added = mf.isAdded();
			boolean is_detached = mf.isDetached();
			int frag_id = mf.getId();

//		DEL:	FldTextView frag_view = (FldTextView) mf.getView();
			FrameLayout fl = (FrameLayout) mf.getView();
			String vstring = "<null>";
			if (fl != null) {
				FldTextView frag_view = (FldTextView) fl.getChildAt(0);
				vstring = Trace.toStringSimple(frag_view);
			}
			String tag = mf.getMyTag();
			// Object	host_obj	= mf.getHost(); // Requires API 23
			String s = String.format(
				"Fragment %s: %b,%b,%#x,%s,%s,%s",
				tag, is_added, is_detached, frag_id,
				vstring, Trace.getIdHc(mf), Trace.toStringSimple(this)
			);
			return s;
		}
	}
}
