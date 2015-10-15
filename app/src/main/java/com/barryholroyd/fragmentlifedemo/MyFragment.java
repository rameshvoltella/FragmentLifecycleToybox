package com.barryholroyd.fragmentlifedemo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Barry on 10/12/2015.
 */
public class MyFragment extends FragmentPrintStates
{
	private Context                 context      = null;
	private int                     frag_no      = 0;
	private String                  frag_tag     = null;// Needed until tag is added in a transaction.
	private int                     container_id = 0;
	private Trace                   trace        = null;

	public void init(Context c, int fno, String ftag, int cid) {
		InfoImpl  info  = new InfoImpl(this);
		trace = new Trace(Trace.LOGTAG_FRAG_DYN, Trace.SEP_FRAGMENT, info);
		super.setLogTag(Trace.LOGTAG_FRAGLC_DYN);
		context = c;
		frag_no = fno;
		frag_tag = ftag;
		container_id = cid;
		trace.log("MyFragment()", String.format("MyFragment(NEW): %s", Trace.getIdHc(this)));
	}

	public int getNumber() { return frag_no; }
	public String getMyTag() { return frag_tag; } // distinct from Fragment.getTag()
	public int getContainerId() { return container_id; }
	public void trace() {
		trace.log("Fragment");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (context == null)
			throw new IllegalStateException("MyFragment not initialized yet.");

		int container_id = container == null ? 0 : container.getId();
		String s = String.format("FldTextView: container_id=%#x", container_id);

		trace.log("onCreateView()", "New: " + s);

		FldTextView fld_tv = new FldTextView(context);
		fld_tv.setText(s);
		return fld_tv;
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
			FldTextView frag_view = (FldTextView) mf.getView();
			String vstring = Trace.toStringSimple(frag_view);
			String tag = mf.getMyTag();
			int hash_code = mf.hashCode();
			// Object	host_obj	= mf.getHost(); // Requires API 23
			String s = String.format(
				"Fragment #%d: %b,%b,%#x,%s,%s,%#x,%s",
				frag_no, is_added, is_detached, frag_id,
				vstring, tag, hash_code, Trace.toStringSimple(this)
			);
			return s;
		}
	}
}
