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
//		super.setLogTag(Trace.LOGTAG_FRAGLC_DYN);
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
	public void trace() {
		trace.log("MyFragment");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		FldTextView fldtv = (FldTextView) inflater.inflate(R.layout.fld_textview, null, false);
		trace.log("onCreateView()",
			String.format("MyFragment: fldtv=%s, container=%s",
				Trace.classAtHc(fldtv), Trace.classAtHc(container)));
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
			String s = String.format(
				"getId()=%#x C@HC=%s Tag=%s Added=%b Attached=%b View=%s",
				mf.getId(), Trace.classAtHc(mf),
				mf.getMyTag(), mf.isAdded(), !mf.isDetached(), viewstr
			);
			return s;
		}
	}
}