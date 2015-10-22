package com.barryholroyd.fragment_lifecycle_toybox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Barry on 10/12/2015.
 */
public class MyFragment extends FragmentPrintStates
{
	static final private String ARG_FTAG            = "ARG_FTAG";
	static final private String ARG_CONTAINER_RID   = "ARG_CONTAINER_RID";

	private InfoImpl  info            = new InfoImpl(this);
	private Trace     trace           = new Trace(Trace.LOGTAG_FRAG_DYN, Trace.SEP_FRAGMENT, info);
	//	DEL: private Trace     tracePs         = new Trace(Trace.LOGTAG_PRINT_STATE, Trace.SEP_PRINT_STATE, info);
	private String    ftag            = "<null>";// Needed until tag is added in a transaction.
	private int       container_rid   = 0;

	public void init(String ftag, int container_rid) {
		this.ftag = ftag;
		this.container_rid = container_rid;
	}

	public String getData() { return info.getData(); }

	public MyFragment() {
		trace.log("MyFragment()", String.format("MyFragment(NEW)=%s",
			Trace.classAtHc(this)));
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
//DEL:	public void tracePs() {
//		tracePs.log("MyFragment");
//	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		trace.log("onCreateView()",
			String.format("MyFragment: container=%s", Trace.classAtHc(container)));
		FldTextView fldtv = (FldTextView) inflater.inflate(R.layout.fld_textview, null, false);
		return fldtv;
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		trace.log("onSaveInstanceState()");
		super.onSaveInstanceState(outState);
		outState.putString(ARG_FTAG, ftag);
		outState.putInt(ARG_CONTAINER_RID, container_rid);
	}
	public class InfoImpl implements Trace.Info
	{
		// "this", from the object that created this instance
		private Object obj = null;

		InfoImpl(Object obj) {
			this.obj = obj;
		}

		public String getData() {
			MyFragment mf = (MyFragment) obj;
			// Object	host_obj	= mf.getHost(); // Requires API 23
			/*
			 * Note: isDetached() returns true only if the fragment has been explicitly
			 *       detached by a call to ft.detach(). When a fragment is first created,
			 *       isDetached() returns false, even though the fragment isn't yet attached
			 *       to an Activity, because it hasn't been explicitly detached with ft.detach().
			 */
			String s = String.format(
				"Tag=%s getId=%#x C@HC=%s Retained=%b isAdded=%b ExplicitlyDetached=%b View=%s",
				mf.getMyTag(), mf.getId(), Trace.classAtHc(mf),
				mf.getRetainInstance(), mf.isAdded(), mf.isDetached(),
				Trace.classAtHc(mf.getView())
			);
			return s;
		}
	}
}
