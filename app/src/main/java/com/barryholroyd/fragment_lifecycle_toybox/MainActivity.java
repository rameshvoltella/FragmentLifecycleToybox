package com.barryholroyd.fragment_lifecycle_toybox;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
 *   o Grep for "FLD" to see relevant log entries from this app. E.g.:
 *     $ adb logcat -v time | grep FLD
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
	static public boolean trace_print_state        = false;
	static public boolean retain_instance         = false; // default for fragment initialization

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
		setButtonLabelItem("3. onCreate()", "Layout & Measure",
			trace_layout_and_measure, R.id.button_toggle_lamt);
		setButtonLabelItem("4. onCreate()", "Print State",
			trace_print_state, R.id.button_toggle_print_state);
		setButtonLabelRetainInstance("5. onCreate()", R.id.buttonset1);
		setButtonLabelRetainInstance("6. onCreate()", R.id.buttonset2);
	}
	private void setButtonLabelItem(String label, String func, boolean toggle, int rid) {
		Button b = (Button) findViewById(rid);
		setButtonLabel(b, label, func, toggle);
	}
	private void setButtonLabelRetainInstance(String label, int rid) {
		ViewGroup vg = (ViewGroup) findViewById(rid);
		Button b = (Button) vg.findViewById(R.id.button_toggle_retain_instance);
		setButtonLabel(b, label, "Retain Instance", retain_instance);
	}
	private void setButtonLabelRetainInstance(String label, View v, boolean toggle) {
		Button b = (Button) v;
		setButtonLabel(b, label, "Retain Instance", toggle);

	}
	private void setButtonLabel(Button b, String label, String func, boolean toggle) {
		String s = toggle ? "on" : "off";
		trace.log(label, String.format("%s turned %s.", func, s));
		b.setText(String.format("%s: %s", func, s));
	}
	//</editor-fold>
	//<editor-fold desc="BUTTONS">
	public void buttonCreateFragment(View v)	{
		int fno = getFragmentNumberForView(v);
		trace.log("buttonCreateFragment", "Fragment #" + fno, true);
		getMyFragmentFromView(v, true); // Just create the fragment; the FragmentManager will manage it.
		printState();
	}
	public void buttonRetainInstance(View v) {
		MyFragment mf = getMyFragmentWrapper(v);
		if (mf == null) {
			trace.log("buttonRetainInstance",
				String.format("Fragment does not exist yet."));
			return;
		}
		boolean ri = !mf.getRetainInstance();
		mf.setRetainInstance(ri);
		setButtonLabelRetainInstance("buttonRetainInstance", v, ri);
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
		setButtonLabelItem("buttonToggleLmTracing", "Layout & Measure",
			trace_layout_and_measure, R.id.button_toggle_lamt);
	}
	public void buttonTogglePrintState(View v) {
		trace_print_state = !trace_print_state;
		setButtonLabelItem("buttonTogglePrintState", "Print State",
			trace_print_state, R.id.button_toggle_print_state);
	}
	//</editor-fold>
	//<editor-fold desc="PRINT METHODS">
	private void printState() {
		if (! MainActivity.trace_print_state)
			return;

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
		MyFragment mf = getMyFragmentFromNumber(fno, false);
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
		int fno = getFragmentNumberForView(v);
		trace.log(label, "Fragment #" + fno, true);
		MyFragment mf = getMyFragmentWrapper(v);
		if (mf == null) {
			trace.log("execFtCommand",
				String.format("Fragment does not exist yet (CMD: %s).", cmd.getString()));
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
		// Handled the case where the fragment doesn't exist yet and isn't created.
		MyFragment mf = getMyFragmentFromView(v, false);
		if (mf == null) {
			int fno = getFragmentNumberForView(v);
			String msg = String.format("Fragment #%d doesn't exist yet.", fno);
			trace.log("getMyFragmentWrapper", msg);
		}
		return mf;
	}
	private MyFragment getMyFragmentFromView(View v, boolean create) {

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
		return getMf(ftag, container_rid, create);
	}
	private MyFragment getMyFragmentFromNumber(int fno, boolean create) {
		String ftag = null;
		int container_rid = 0;
		switch (fno) {
			case 1:
				ftag = FRAGTAG1;
				container_rid = R.id.container1;
				break;
			case 2:
				ftag = FRAGTAG2;
				container_rid = R.id.container2;
				break;
			default:
				throw new IllegalStateException("Bad fragment number: " + fno);
		}
		return getMf(ftag, container_rid, create);
	}
	private MyFragment getMf(String ftag, int container_rid, boolean create) {
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
