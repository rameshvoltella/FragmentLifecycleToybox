package com.barryholroyd.fragmentlifedemo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

//<editor-fold desc="APPLICATION NOTES">
/*
 * Application Notes
 *   o Rotating the device will cause the log pane to lose its previous text. This could be
 *     managed by use ove onSaveInstanceState(), but that isn't really critical.
 */
//</editor-fold>

public class MainActivity extends ActivityPrintStates
{
	//<editor-fold desc="FIELDS">
	private Trace trace = null;
	private FragmentManager fm = getFragmentManager();

	// Transient storage for MyFragments, to track when they aren't in the FragmentManager.
	private HashMap<String,MyFragment> transientMyFragments = new HashMap<>();

	static final private String FRAGTAG1 = "FragTag1";
	static final private String FRAGTAG2 = "FragTag2";

	// Tracing onLayout() and onMeasure() introduces a lot of extra tracing.
	static public boolean trace_layout_and_measure = false;

	private enum FTCMD {    // FragmentTransaction commands
		ADD_WITHOUT_VIEW("ADD_WITHOUT_VIEW"),
		ADD_WITH_VIEW("ADD_WITH_VIEW"),
		REMOVE("REMOVE"),
		REPLACE("REPLACE"),
		DETACH("DETACH"),
		ATTACH("ATTACH"),
		HIDE("HIDE"),
		SHOW("SHOW"),
		;

		private String cmdname = null;
		FTCMD(String name) {
			cmdname = name;
		}
		String getString() { return cmdname; }
	}

	//</editor-fold>
	//<editor-fold desc="OVERRIDDEN METHODS">
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		trace = new Trace(Trace.LOGTAG_APP, Trace.SEP_APP, null);
		trace.log("1. onCreate()",
			String.format(
				"Before setContentView(): R.id.buttonset1=%#x, R.id.buttonset2=%#x",
				R.id.buttonset1, R.id.buttonset2));
		setContentView(R.layout.activity_main);
		Trace.init(this);
		trace.log("2. onCreate()", "After setContentView(): Log Pane:[INITIALIZED].");

		Button b = (Button) findViewById(R.id.button_toggle_lamt);
		String s = trace_layout_and_measure ? "on" : "off";
		b.setText(String.format("Layout & Measure: %s", s));
	}
	//</editor-fold>
	//<editor-fold desc="BUTTONS">
	public void buttonCreateFragment(View v)	{
		int fno = getFragmentNumberForView(v);
		trace.log("buttonCreateFragment", "Fragment #" + fno, true);
		getMyFragment(v, true); // Just create the fragment; the FragmentManager will manage it.
		printState();
	}
	public void buttonAddFragmentWithView(View v)		{
		execFtCommand("buttonAddFragmentWithView", v, FTCMD.ADD_WITH_VIEW);
	}
	public void buttonAddFragmentWithoutView(View v)		{
		/*
		 * NOTE: If you subsequently get the view from the fragment (e.g., using
		 *       v=fragment.getView()) and add it to a View Group (e.g., vg.addView(v)),
		 *       then you need to manage that attachment yourself. For example, if you
		 *       subsequently call fragment_transaction.remove(fragment), that would
		 *       detach the View from its parent ViewGroup if it knew about it, but in
		 *       this case it wouldn't, so the View would remain in place. Furthermore,
		 *       the reference to the View from the fragment would go away, to anything
		 *       subsequently done with the fragment (e.g., fragment.hide()) would not
		 *       affect the View. The View and its parent would have become 100% independent
		 *       of the fragment.
		 */
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
	public void buttonToggleLmTracing(View v) {
		trace_layout_and_measure = !trace_layout_and_measure;
		String s = trace_layout_and_measure ? "on" : "off";
		trace.log("buttonToggleLmTracing",
			String.format("Layout & Measure tracing turned %s.", s));
		Button b = (Button) v;
		b.setText(String.format("Layout & Measure: %s", s));
	}
	//</editor-fold>
	//<editor-fold desc="PRINT METHODS">
	private void printState() {
		trace.log("[PRINT STATE START]", String.format("Transient MFs: %s.", getTransientMfs()));

		// Print Fragments information
		printFragmentInfo(1);
		printFragmentInfo(2);

		// Print Containers information
		printContainerInfo(R.id.container1);
		printContainerInfo(R.id.container2);
		trace.log("[PRINT STATE END]");
	}
	private void printFragmentInfo(int fno) {
		String ftag = null;
		switch (fno) {
			case 1: ftag = FRAGTAG1; break;
			case 2: ftag = FRAGTAG2; break;
		}
		FragmentManager fm = getFragmentManager();
		MyFragment mf = (MyFragment) fm.findFragmentByTag(ftag);

		trace.log("printFragmentInfo",
			String.format("Fragment %s: %s in FragmentManager.",
				ftag, mf == null ? "not" : ""));

		if (mf != null)
			mf.trace();
	}
	private String getTransientMfs() {
		Set keys = transientMyFragments.keySet();
		Iterator<String> iter = keys.iterator();
		String s = "";
		while (iter.hasNext()) {
			String key = iter.next();
			MyFragment mf = transientMyFragments.get(key);
			if (!s.equals("")) {s += ", ";}
			s += String.format("%s=%#x", key, mf.hashCode());
		}
		return s == null ? "" : s;
	}
	private void printContainerInfo(int cid) {
		FldLinearLayout fld_ll = (FldLinearLayout) findViewById(cid);
		if (fld_ll == null) {
			String msg = String.format("No container for id: %#x!", cid);
			trace.log("printContainerInfo", msg);
			throw new IllegalStateException(msg);
		}
		trace.log("printContainerInfo");
		fld_ll.fldLlTrace();
	}
	//</editor-fold>
	//<editor-fold desc="SUPPORT METHODS">
	private void execFtCommand(String label, View v, FTCMD cmd) {
//	DEL:	String fno = Integer.toString(getFragmentNumberForView(v));
		int fno = getFragmentNumberForView(v);
		trace.log(label, "Fragment #" + fno, true);
		MyFragment mf = getMyFragmentWrapper(v);
		if (mf == null) {
			trace.log("execFtCommand",
				String.format("Fragment does not exit yet (CMD: %s).", cmd.getString()));
			return;
		}
		/*
		 * We can't catch exceptions in the code below (and continue, having reported them)
		 *   because when that happens executePendingTransactions() apparently never completes
		 *   and we get a "java.lang.IllegalStateException: Recursive entry to
		 *   executePendingTransactions" exception the next time we enter this method. Because
		 *   of that, we have to catch exceptional conditions in advance and live with any
		 *   unexpected exceptions (i.e., the app will crash).
		 *
		 * TBD: find some way to deal with the recursive executePendingTransactions error.
		 */

		/*
		 * SUMMARY:
		 *   o Add and Delete add/delete the fragment from the FragmentManager.
		 *   o Attach and Detach do not -- they leave the fragment in the Fragment Manager.
		 *
		 * Add:    Add a fragment to the "Activity State".
		 *         Optionally have its View inserted into a container.
		 * Remove: Remove a fragment from the "Activity State".
		 *         If it was added to a container, its view is also removed from that container.
		 * Detach: Detach the given fragment from the UI.
		 *         This is the same state as when it is put on the back stack:
		 *           o the fragment is removed from the UI.
		 *           o its state is still being actively managed by the fragment manager.
		 *         When going into this state its view hierarchy is destroyed.
		 * Attach: Re-attach a fragment after it had previously been detached from the UI with detach(Fragment).
		 *         This causes its view hierarchy to be re-created, attached to the UI, and displayed.
		 */
		FragmentTransaction ft = fm.beginTransaction();
		String ftag = mf.getMyTag();
		int cid = mf.getContainerId();

		switch(cmd) {
			case ADD_WITHOUT_VIEW:
				trace.logCode("ft.add(mf, ftag);");
				if (fragmentInFragmentManager(label, ftag))
					return;
				ft.add(mf, ftag);
				transientMyFragments.remove(ftag);
				break;
			case ADD_WITH_VIEW:
				trace.logCode("ft.add(cid, mf, ftag);");
				if (fragmentInFragmentManager(label, ftag))
					return;
				ft.add(cid, mf, ftag);
				transientMyFragments.remove(ftag);
				break;
			case REMOVE:
				ft.remove(mf);
				transientMyFragments.put(ftag, mf);
				trace.logCode("ft.remove(mf);");
				break;
			case REPLACE:
				ft.replace(cid, mf, ftag);
				trace.logCode("ft.replace(cid, mf, ftag);");
				break;
			case DETACH:
				ft.detach(mf);
				trace.logCode("ft.detach(mf);");
				break;
			case ATTACH:
				ft.attach(mf);
				trace.logCode("ft.attach(mf);");
				break;
			case HIDE:
				ft.hide(mf);
				trace.logCode("ft.hide(mf);");
				break;
			case SHOW:
				ft.show(mf);
				trace.logCode("ft.show(mf);");
				break;
		}
		ft.commit();
		fm.executePendingTransactions();
		printState();
	}
	private MyFragment getMyFragmentWrapper(View v) {
		MyFragment mf = getMyFragment(v, false);
		if (mf == null) {
			int fno = getFragmentNumberForView(v);
			String msg = String.format("Fragment #%d doesn't exist yet.", fno);
			trace.log("getMyFragmentWrapper", msg);
		}
		return mf;
	}
	private MyFragment getMyFragment(View v, boolean create) {

		// Get the fragment corresponding to the button's container.
		Button button = (Button) v;
		LinearLayout ll = (LinearLayout) button.getParent();
		int button_parent_id = ll.getId();
		int container_rid = 0;

		String ftag = null;
		switch (button_parent_id) {
			case R.id.buttonset1:
				ftag = FRAGTAG1;
				container_rid = R.id.container1;
				break;
			case R.id.buttonset2:
				ftag = FRAGTAG2;
				container_rid = R.id.container2;
				break;
			default:
				throw new IllegalStateException("Bad container id: " + button_parent_id);
		}

		MyFragment mf = transientMyFragments.get(ftag);
		if (mf != null) {
			trace.log("getMyFragment()",
				String.format(
					"EXISTING fragment retrieved from transient storage: %s (%s).",
					mf.getMyTag(), Trace.classAtHc(mf)));
			return mf;
		}

		FragmentManager fm = getFragmentManager();
		mf = (MyFragment) fm.findFragmentByTag(ftag);
		if (mf == null) {
			if (create) {
				mf = new MyFragment();
				mf.init(ftag, container_rid);
				transientMyFragments.put(ftag, mf);
				trace.log("getMyFragment()",
					String.format(
						"NEW fragment created: %s (%s) ",
						mf.getMyTag(), Trace.classAtHc(mf)));
				trace.logCode("mf = new MyFragment();");
			}
			else
				trace.log("getMyFragment()", "No fragment yet.");

		} else
			trace.log("getMyFragment()",
				String.format("Existing fragment in FragmentManager: %s(%s)",
					mf.getMyTag(),
					Trace.classAtHc(mf)));

		return mf;
	}
	private int getFragmentNumberForView(View v) {
		switch (((ViewGroup)v.getParent()).getId()) {
			case R.id.buttonset1:   return 1;
			case R.id.buttonset2:   return 2;
			default:                return 0;
		}
	}
	private boolean fragmentInFragmentManager(String label, String ftag) {
		if (fm.findFragmentByTag(ftag) != null) {
			trace.log(label,
				String.format("%s is already in the FragmentManager.", ftag));
			return true;
		}
		return false;
	}
//</editor-fold>
}
