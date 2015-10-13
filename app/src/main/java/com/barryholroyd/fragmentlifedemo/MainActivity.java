package com.barryholroyd.fragmentlifedemo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends ActivityPrintStates
{
	//<editor-fold desc="FIELDS">
	static public Bhlogger bh = new Bhlogger("FLD APP");
	private Trace trace = null;
	private FragmentManager fm = getFragmentManager();

	static final private String FRAGTAG1 = "FragTag1";
	static final private String FRAGTAG2 = "FragTag2";

	private enum FTCMD {    // FragmentTransaction commands
		ADD_WITHOUT_VIEW,
		ADD_WITH_VIEW,
		REMOVE,
		REPLACE,
		DETACH,
		ATTACH,
		HIDE,
		SHOW
	}

	//</editor-fold>
	//<editor-fold desc="OVERRIDDEN METHODS">
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InfoImpl  info  = new InfoImpl(this);
		trace = new Trace("FLD APP", Trace.SEP_APP, info);
		trace.log("onCreate", String.format("%s: %#x", "R.id.buttonset1", R.id.buttonset1));
		trace.log("onCreate", String.format("%s: %#x", "R.id.buttonset2", R.id.buttonset2));
		setContentView(R.layout.activity_main);
	}
	//</editor-fold>
	//<editor-fold desc="BUTTONS">
	public void buttonCreateFragment(View v)	{
		tracesep("buttonCreateFragment");
		getMyFragment(v, true); // Just create the fragment; the FragmentManager will manage it.
		printState();
	}
	public void buttonAddFragmentWithView(View v)		{
		execFtCommand("buttonAddFragmentWithView", v, FTCMD.ADD_WITH_VIEW);
	}
	public void buttonAddFragmentWithoutView(View v)		{
		execFtCommand("buttonAddFragmentWithoutView", v, FTCMD.ADD_WITHOUT_VIEW);
	}
	public void buttonRemoveFragment(View v)	{
		execFtCommand("buttonRemoveFragment", v, FTCMD.REMOVE);
	}
	public void buttonReplaceFragment(View v)	{
		execFtCommand("buttonReplaceFragment", v, FTCMD.REPLACE);
	}
	public void buttonAttachFragment(View v)	{
		// From: http://developer.android.com/reference/
		//       android/app/FragmentTransaction.html#attach
		//       (android.app.Fragment)
		// Re-attach a fragment after it had previously been
		//   detached from the UI with detach(Fragment). This causes
		//   its view hierarchy to be re-created, attached to the UI,
		//   and displayed.
		execFtCommand("buttonAttachFragment", v, FTCMD.ATTACH);
	}
	public void buttonDetachFragment(View v)	{
		// From: http://developer.android.com/reference/
		//       android/app/FragmentTransaction.html#detach
		//       (android.app.Fragment)
		// Detach the given fragment from the UI. This is the same
		//   state as when it is put on the back stack: the fragment
		//   is removed from the UI, however its state is still being
		//   actively managed by the fragment manager. When going into
		//   this state its view hierarchy is destroyed.
		execFtCommand("buttonDetachFragment", v, FTCMD.DETACH);
	}
	public void buttonHideFragment(View v)		{
		// This will cause the fragment's View to be hidden.
		execFtCommand("buttonHideFragment", v, FTCMD.HIDE);
	}
	public void buttonShowFragment(View v)		{
		// This will cause the fragment's View to be shown.
		execFtCommand("buttonShowFragment", v, FTCMD.SHOW);
	}
	public void buttonAddFragmentView(View v)		{
		trace.log("buttonAddFragmentView");
		MyFragment mf = getMyFragmentWrapper(v);
		if (mf == null)
			return;

		View view = mf.getView();
		if (view == null) {
			trace.log("buttonAddFragmentView", "Fragment does not have a view.");
			return;
		}
		int container_id = mf.getContainerId();
		FldLinearLayout fld_ll = (FldLinearLayout) findViewById(container_id);
		fld_ll.addView(view);
		printState();
	}
	public void buttonRemoveFragmentView(View v)		{
		trace.log("buttonRemoveFragmentView");
		MyFragment mf = getMyFragmentWrapper(v);
		if (mf == null)
			return;

		View view = mf.getView();
		if (view == null) {
			trace.log("buttonRemoveFragmentView", "Fragment does not have a view.");
			return;
		}
		int container_id = mf.getContainerId();
		FldLinearLayout fld_ll = (FldLinearLayout) findViewById(container_id);
		fld_ll.removeView(view);
		printState();
	}
	//</editor-fold>
	//<editor-fold desc="PRINT METHODS">
	private void printState() {
		// Print Fragments information

		printFragmentInfo(1);
		printFragmentInfo(2);

		// Print Containers information
		printContainerInfo(R.id.container1);
		printContainerInfo(R.id.container2);
	}
	private void printFragmentInfo(int fno) {
		String ftag = null;
		switch (fno) {
			case 1: ftag = FRAGTAG1; break;
			case 2: ftag = FRAGTAG2; break;
		}
		FragmentManager fm = getFragmentManager();
		MyFragment mf = (MyFragment) fm.findFragmentByTag(ftag);
		if (mf == null) {
			trace.log("printFragmentInfo",
				String.format("No Fragment #%d yet.", fno));
			return;
		}
		mf.trace();
	}
	private void printContainerInfo(int cid) {
		FldLinearLayout fld_ll = (FldLinearLayout) findViewById(cid);
		if (fld_ll == null) {
			String msg = String.format("No container for id: %#x!", cid);
			trace.log("printContainerInfo", msg);
			throw new IllegalStateException(msg);
		}
		fld_ll.fldLlTrace();
	}
	//</editor-fold>

	private void tracesep(String label) {
		bh.log("-----------------------------\n");
		trace.log(label);
	}
	//<editor-fold desc="SUPPORT METHODS">
	private void execFtCommand(String label, View v, FTCMD cmd) {
		tracesep(label);
		MyFragment mf = getMyFragmentWrapper(v);
		if (mf == null)
			return;
		FragmentTransaction ft = fm.beginTransaction();
		String ftag = mf.getMyTag();
		int cid = mf.getContainerId();
		switch(cmd) {
			case ADD_WITHOUT_VIEW:       ft.add(mf, ftag); break;
			case ADD_WITH_VIEW:       ft.add(cid, mf, ftag); break;
			case REMOVE:   break;
			case REPLACE:   break;
			case DETACH:   break;
			case ATTACH:   break;
			case HIDE:   break;
			case SHOW:   break;
		}
		ft.commit();
		printState();
	}
	private MyFragment getMyFragmentWrapper(View v) {
		MyFragment mf = getMyFragment(v, false);
		if (mf == null) {
			int fno = getFragmentNumberForView(v);
			String msg = String.format("Fragment #%d doesn't exist yet.", fno);
			trace.log("buttonAddFragmentWithView", msg);
		}
		return mf;
	}
	private int getFragmentNumberForView(View v) {
		switch (((ViewGroup)v.getParent()).getId()) {
			case R.id.buttonset1:   return 1;
			case R.id.buttonset2:   return 2;
			default:                return 0;
		}
	}
	private MyFragment getMyFragment(View v, boolean create) {

		// Get the fragment corresponding to the button's container.
		Button button = (Button) v;
		FldLinearLayout fld_ll = (FldLinearLayout) button.getParent();
		int container_id = fld_ll.getId();

		int frag_no = 0;
		String frag_tag = null;
		switch (container_id) {
			case R.id.buttonset1:
				frag_no = 1;
				frag_tag = FRAGTAG1;
				break;
			case R.id.buttonset2:
				frag_no = 2;
				frag_tag = FRAGTAG2;
				break;
			default:
				throw new IllegalStateException("Bad container id: " + container_id);
		}

		FragmentManager fm = getFragmentManager();
		MyFragment mf = (MyFragment) fm.findFragmentByTag(frag_tag);
		if (mf == null) {
			if (create) {
				mf = new MyFragment();
				mf.init(this, frag_no, frag_tag, container_id);
				trace.log("getMyFragment()",
					String.format("New fragment: %#x", Trace.getId(mf)));
			}
			else
				trace.log("getMyFragment()", "No fragment yet.");

		} else
			trace.log("getMyFragment()",
				String.format("Existing fragment: %#x", Trace.getId(mf)));

		return mf;
	}

//</editor-fold>
	class InfoImpl implements Trace.Info {
		// "this", from the object that created this instance
		private Object obj = null;

	InfoImpl (Object obj) {
			this.obj = obj;
		}

		public String getData() {
			return String.format("This:%-22s", Trace.toStringSimple(obj));
		}
	}
}

class Trace {
	static final public String SEP_APS = "|";
	static final public String SEP_APP = "  |";
	static final public String SEP_FRG = "    |";
	static final public String SEP_VGP = "      |";
	static final public String SEP_VEW = "        |";
	static final public String SEP_PRSTATE  = "          >";

	private Bhlogger    bh   = null;
	private Info        info = null;
	private String      sep  = null;

	interface Info {
		public String getData();
	}

	Trace(String logtag, String _sep, Info _info) {
		bh     = new Bhlogger(logtag);
		sep    = _sep;
		info   = _info;
	}

	public void log(String label) { log(label, ""); }
	public void log(String label, String msg) {
		String data = info.getData();
		writelog(sep, label, data, msg);
	}
	// Main trace method. Used by all trace methods.
	private void writelog(String sep, String label, String data, String msg) {
		String leader = String.format("%s %-20s: ", sep, label);
		String s = String.format("%-30s Data:[%s] Msg:[%s]", leader, data, msg);
		bh.log(s);
	}
	static public String toStringSimple(Object obj) {
		return obj.getClass().getSimpleName() + '@' + Integer.toHexString(getId(obj));
	}
	static public int getId(Object obj) {
		return obj.hashCode();
	}
}
