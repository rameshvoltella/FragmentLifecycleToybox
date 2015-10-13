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
	static final private Bhlogger bh = new Bhlogger("FLD MYF");
	private Context context          = null;
	MyFragment my_fragment           = null;
	int hash_val                     = 0;

	public MyFragment() {
		hash_val = this.hashCode();
		bh.log(String.format(
			"MyFragment(NEW): %#x",
			this.hashCode()));
	}

	public static MyFragment newInstance(Context c) {
		MyFragment mf = new MyFragment();
		mf.init(c);
		bh.log("MyFragment.newInstance: new fragment");
		return mf;
	}

	private void init(Context c) {
		context = c;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (context == null)
			throw new IllegalStateException("MyFragment not initialized yet.");

		int container_id = container == null ? 0 : container.getId();
		FldTextView fld_tv = new FldTextView(context);
		int fld_tv_id = fld_tv.getId();

		trace("onCreateView()",
			String.format(
				"creating and return new FldTextView; id=%#x; container id: %#x",
				container_id));

		return fld_tv;
	}

	private void trace(String label) {
		trace(label, "");
	}
	private void trace(String label, String msg) {
		String s = String.format("%s%s", MainActivity.SEP_FRAG,getMyFragmentInfo(label, msg));
		bh.log(s);
	}
	public String getMyFragmentInfo(String label, String msg) {
		String s = String.format(
			"%-15s [%#x] (%s): %s",
			label, this.getId(), this.toStringSimple(), msg);
		return s;
	}
	public String toStringSimple() {
		return getClass().getSimpleName() + '@' + Integer.toHexString(hashCode());
	}
}
