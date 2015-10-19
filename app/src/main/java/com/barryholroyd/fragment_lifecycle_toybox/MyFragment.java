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
		trace.log("MyFragment()", String.format("MyFragment(NEW)=%s",
			Trace.classAtHc(this)));
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(MainActivity.retain_instance);
		if (savedInstanceState != null) {
			ftag            = savedInstanceState.getString(ARG_FTAG);
			container_rid   = savedInstanceState.getInt(ARG_CONTAINER_RID);
		}
	}

	public String getMyTag() { return ftag; } // distinct from Fragment.getTag()
	public int getContainerId() { return container_rid; }
	public void tracePs() {
		MainActivity.tracePs.log("MyFragment");
	}

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

	class InfoImpl implements Trace.Info
	{
		// "this", from the object that created this instance
		private Object obj = null;

		InfoImpl(Object obj) {
			this.obj = obj;
		}

		public String getData() {
			MyFragment mf = (MyFragment) obj;
			// Object	host_obj	= mf.getHost(); // Requires API 23
			FldTextView fldtv = (FldTextView) mf.getView();
			String viewstr = fldtv == null
				? "<null>" : Trace.classAtHc(fldtv);
			/*
			 * Note: isDetached() returns true only if the fragment has been explicitly
			 *       detached by a call to ft.detach(). When a fragment is first created,
			 *       isDetached() returns false, even though the fragment isn't yet attached
			 *       to an Activity, because it hasn't been explicity detached with ft.detach().
			 */
			String s = String.format(
				"getId()=%#x C@HC=%s Tag=%s Added=%b ExplicitlyDetached=%b View=%s",
				mf.getId(), Trace.classAtHc(mf),
				mf.getMyTag(), mf.isAdded(), mf.isDetached(), viewstr
			);
			return s;
		}
	}
}
