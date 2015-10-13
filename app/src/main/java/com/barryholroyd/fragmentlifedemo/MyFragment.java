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
	static final private Bhlogger   bh           = new Bhlogger("FLD MYF");
	private Context                 context      = null;
	private int                     frag_no      = 0;
	private String                  frag_tag     = null;// Needed until tag is added in a transaction.
	private int                     container_id = 0;
	private int                     hash_val     = 0;
	private Trace                   trace        = null;

	public void init(Context c, int fno, String ftag, int cid) {
		InfoImpl  info  = new InfoImpl(this);
		trace = new Trace("FLD FRG", Trace.SEP_FRG, info);
		context = c;
		frag_no = fno;
		frag_tag = ftag;
		container_id = cid;
		hash_val = this.hashCode();
		trace.log("MyFragment()", String.format("MyFragment(NEW): %#x", this.hashCode()));
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
		FldTextView fld_tv = new FldTextView(context);
		int fld_tv_id = fld_tv.getId();

		trace.log("onCreateView()",
			String.format(
				"New FldTextView: view_id=%#x, container_id=%#x", fld_tv_id, container_id));

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
			String vstring = frag_view.toStringSimple();
			String tag = mf.getMyTag();
			int hash_code = mf.hashCode();
			// Object	host_obj	= mf.getHost(); // Requires API 23
			String s = String.format(
				"Fragment #%d: %b,%b,%#x,%s,%s,%#x,%s",
				frag_no,
				is_added,
				is_detached,
				frag_id,
				vstring,
				tag,
				hash_code,
				Trace.toStringSimple(this)
			);
			return s;
		}
	}
}
