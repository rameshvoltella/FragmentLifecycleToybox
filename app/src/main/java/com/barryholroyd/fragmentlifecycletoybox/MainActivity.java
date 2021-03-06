package com.barryholroyd.fragmentlifecycletoybox;

import android.app.Fragment;
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
 *     managed by use of onSaveInstanceState(), but that isn't really critical.
 *   o Grep for "FLD" to see relevant log entries from this app. E.g.:
 *     $ adb logcat -v time | grep FLD
 */
//</editor-fold>

public class MainActivity extends ActivityPrintStates
{
	//<editor-fold desc="FIELDS">
	private Trace trace = new Trace(Trace.LOGTAG_APP, Trace.SEP_APP, null);

	// "Transient Fragment Manager"
	// This isn't an actual Android FragmentManager. It tracks fragments which exist
	//   but which are *not* in the actual FragmentManager.
	private HashMap<String,MyFragment> transientMyFragments = new HashMap<>();

	static final protected String FRAGTAG_D1 = "FragTagDynamic1";
	static final protected String FRAGTAG_D2 = "FragTagDynamic2";
	static final protected String FRAGTAG_S1 = "FragTagStatic1"; // static fragment

	static final protected int FRAGCONTAINER_D1 = R.id.container1;
	static final protected int FRAGCONTAINER_D2 = R.id.container2;
	static final protected int FRAGCONTAINER_S1 = R.id.container3;

	// Tracing onLayout() and onMeasure() introduces a lot of extra tracing.
	static public boolean trace_layout_and_measure = false;

	static private FragmentManager fm = null;

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
		fm = getFragmentManager();
		trace.log("1. onCreate()",
			String.format(
				"Before setContentView(): R.id.buttonset1=%#x, R.id.buttonset2=%#x",
				R.id.buttonset1, R.id.buttonset2));
		setContentView(R.layout.activity_main);
		Trace.init(this);
		trace.log("2. onCreate()", "After setContentView(): Log Pane:[INITIALIZED].");
		setButtonLabelItem("3. onCreate()", "Layout & Measure",
			trace_layout_and_measure, R.id.button_toggle_lamt);
		setButtonLabelRetainInstanceBlank("4. onCreate()", 1);
		setButtonLabelRetainInstanceBlank("5. onCreate()", 2);
		setButtonLabelRetainInstanceBlank("5. onCreate()", 3);
		disableStaticFragmentButtons();
	}
	private void disableStaticFragmentButtons() {
		ViewGroup vg = (ViewGroup) findViewById(R.id.buttonset3);
		disableStaticFragmentButton(vg, R.id.button_create);
	}
	private void disableStaticFragmentButton(ViewGroup vg, int rid) {
		Button b = (Button) vg.findViewById(rid);
		b.setEnabled(false);
		b.setText("<static>");
	}
	//</editor-fold>
	//<editor-fold desc="BUTTONS">
	public void buttonCreateFragment(View v)	{
		int fno = getFragmentNumberForView(v);
		trace.log("buttonCreateFragment", "Fragment #" + fno, true);
		getMyFragmentFromView(v, true); // Just create the fragment; the FragmentManager will manage it.
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
		Button b = (Button) v;
		setButtonLabelRetainInstance("buttonRetainInstance", b, ri);
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
	public void buttonPrintState(View v) {
		printState();
	}
	public void buttonReset(View v) {
		trace.log("Resetting (fully destroying all *dynamic* fragments).");
		destroyFragment(FRAGTAG_D1);
		destroyFragment(FRAGTAG_D2);
		// Retain the static fragment since it can't be re-created.
		MyFragment mf = transientMyFragments.get(FRAGTAG_S1);
		transientMyFragments.clear();
		if (mf != null)
			transientMyFragments.put(FRAGTAG_S1, mf);
	}
	private void destroyFragment(String ftag) {
		FragmentManager fm = getFragmentManager();
		MyFragment mf = (MyFragmentDynamic) fm.findFragmentByTag(ftag);
		if (mf == null || mf.getMyTag().equals(FRAGTAG_S1))
			return;
		FragmentTransaction ft = fm.beginTransaction();
		ft.remove(mf);
		ft.commit();
		fm.executePendingTransactions();
	}
	//</editor-fold>
	//<editor-fold desc="BUTTON LABELS">
	private void setButtonLabelItem(String label, String func, boolean toggle, int rid) {
		Button b = (Button) findViewById(rid);
		setButtonLabel(b, label, func, toggle);
	}
	private void setButtonLabel(Button b, String label, String func, boolean toggle) {
		String s = String.format("%s: %s", func, toggle ? "on" : "off");
		trace.log(label, s + '.');
		b.setText(s);
	}
	private void setButtonLabelRetainInstanceBlank(String label, int fno) {
		FragmentIdSet fid_set = FragmentIdSet.newInstanceFromFragmentNumber(fno);
		MyFragment mf = getMyFragment(fid_set, false, true);
		ViewGroup vg = (ViewGroup) findViewById(fid_set.getButtonSetRid()); // button set
		Button b = (Button) vg.findViewById(R.id.button_toggle_retain_instance);
		if (mf == null)
			b.setText("Retain Instance");
		else
			setButtonLabelRetainInstance(label, b, mf.getRetainInstance());
	}
	private void setButtonLabelRetainInstance(String label, Button b, boolean toggle) {
		setButtonLabel(b, label, "Retain Instance", toggle);
	}
	private void setButtonLabelRetainInstance(String label, int fno, boolean toggle) {
		int rid = FragmentIdSet.newInstanceFromFragmentNumber(fno).getButtonSetRid();
		ViewGroup vg = (ViewGroup) findViewById(rid); // button set
		Button b = (Button) vg.findViewById(R.id.button_toggle_retain_instance);
		setButtonLabel(b, label, "Retain Instance", toggle);
	}
	//</editor-fold>
	//<editor-fold desc="METHODS: SUPPORT">
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

		/*
		 * STATIC FRAGMENTS
		 * I experimented with running FragmentTransaction methods on static
		 * fragments, to see what would happen. That scenario appears to be
		 * not well tested and fairly buggy, most likely because (I believe that)
		 * FragmentTransactions were never intended to be used with static fragments.
		 *
		 * The bottom line is:
		 *  FRAGMENT TRANSACTIONS SHOULD PROBABLY NOT BE USED ON STATIC FRAGMENTS.
		 */
		FragmentTransaction ft = fm.beginTransaction();
		String ftag = mf.getMyTag();
		int cid = mf.getContainerId();

		switch(cmd) {
			case ADD_WITHOUT_VIEW:
				trace.logCode("ft.add(mf, ftag);");
				if (fragmentInFragmentManager(ftag, label))
					return;
				ft.add(mf, ftag);
				transientMyFragments.remove(ftag);
				break;
			case ADD_WITH_VIEW:
				if (isStaticContainerFragment(cid)) {
					trace.logCode(
						"Skipping ft.add(cid, mf, ftag) for static fragment " +
						"\nto avoid putting it into a bad state.");
					return;
				}
				trace.logCode("ft.add(cid, mf, ftag);");
				if (fragmentInFragmentManager(ftag, label))
					return;
				ft.add(cid, mf, ftag);
				transientMyFragments.remove(ftag);
				break;
			case REMOVE:
				trace.logCode("ft.remove(mf);");
				ft.remove(mf);
				transientMyFragments.put(ftag, mf);
				break;
			case REPLACE:
				trace.logCode("ft.replace(cid, mf, ftag);");
				if (isStaticContainerFragment(cid))
				  return;
				ft.replace(cid, mf, ftag);
				transientMyFragments.remove(ftag);
				break;
			case DETACH:
				/*
				 * There appears to be a bug in ft.detach() code. It allows you to detach
				 *   (isDetach() will return true) even if you haven't yet been added to
				 *   the FragmentManager (e.g., via ft.add()). If you subsequentl call
				 *   ft.attach(), it will (under the covers) then run ft.add(), silently
				 *   adding you to the FragmentManager. However, it will do so without
				 *   specifying a tag, since it apparently assumes that you already have
				 *   one, if you wanted one. After that, if you run one of the "ADD"
				 *   commands above, the check for the fragment already being in the
				 *   FragmentManager will fail (because the fragment didn't have the
				 *   necessary tag when it was entered) and the subsequent ft.add() will
				 *   throw an Illegal State exception, indicating that you have already added
				 *   the fragment. So we have to check for the fragment already being in the
				 *   FragmentManager here manually, rather than relying on ft.detach() to do so.
				 */
				if (! fragmentInFragmentManager(ftag, label)) {
					trace.logCode("Skipping ft.detach(): not added yet.");
					return;
				}
				trace.logCode("ft.detach(mf);");
				ft.detach(mf);
				break;
			case ATTACH:
				ft.attach(mf);
				trace.logCode("ft.attach(mf);");
				break;
			case HIDE:
				trace.logCode("ft.hide(mf);");
				ft.hide(mf);
				break;
			case SHOW:
				trace.logCode("ft.show(mf);");
				ft.show(mf);
				break;
		}
		ft.commit();
		fm.executePendingTransactions();
	}
	private boolean isStaticContainerFragment(int cid) {
		/*
		 * FragmentTransaction methods should probably not be called on static
		 * fragments. At least two problems can occur.
		 *
		 * REPLACE()
		 * Most likely ft.replace() should not be called on a static fragment.
		 * Still, it should minimally throw an appropriate exception or, even better,
		 * simply do the replace (most of the other functions here work on static
		 * fragments).
		 *
		 * Instead, there seems to be a bug in ft.replace().
		 *
		 *   o The container id (cid) value (in the current compile) is 2131492976.
		 *   o 2131492976 is the parent ViewGroup of the <fragment>.
		 *   o 2131492977 is the View that has replaced the <fragment> tag
		 *
		 * This should be fine -- the cid represents the correct container for the
		 * fragment. However, it throws the following exception:
		 *
		 *  java.lang.IllegalStateException: Can't change container ID of fragment
		 *    MyFragmentStatic{38a9c6c2 #0 id=0x7f0c0071 FragTagStatic1}:
		 *    was 2131492977 now 2131492976
		 *
		 * It appears that replace() mistakenly believes that the View is actually
		 *   the ViewGroup.
		 *
		 * So we check for this condition, to work around the bug.
		 *
		 * ADD (with a view)
		 * If a fragment is removed from the FragmentManager (which we check for
		 * before running ft.add() on it), and then added via ft.add(cid, mf, ftag),
		 * so that its View is put into a container, the fragment seems to end up
		 * in a weird state. More specifically, if the device is then rotated the
		 * static fragment will get recreated out of the XML file, as it should,
		 * but its onCreateView() method will not be called. Soon after, when the
		 * view hierarchy is getting laid out, the system will throw the following
		 * exception.
		 *
		 * 	java.lang.IllegalStateException: Fragment
         *    com.barryholroyd.fragmentlifecycletoybox.MyFragmentStatic
         *    did not create a view.
		 *
		 * Since it is in the FragmentManager, the FM knows about it and probably
		 * intends to recreate a new "dynamic" instance of it. Somehow that seems
		 * to interfere with the creation of the fragment as a static fragment --
		 * specifically, by preventing it onCreateView() method from being called.
		 *
		 * In our code here, we disallow the running of ftadd(cid, mf, ftag) on the
		 * static fragment.
		 */
		 return cid == FRAGCONTAINER_S1;
	}
	private boolean fragmentInFragmentManager(String ftag, String label) {
		if (fm.findFragmentByTag(ftag) != null) {
			if (label != null) {
				trace.log(label,
					String.format("%s is already in the FragmentManager.", ftag));
			}
			return true;
		}
		return false;
	}
	private int getFragmentNumberForView(View v) {
		int buttonset_rid = ((ViewGroup)v.getParent()).getId(); // button set that the button is in
		return FragmentIdSet.newInstanceFromButtonsetRid(buttonset_rid).getFragmentNumber();
	}
//</editor-fold>
	//<editor-fold desc="METHODS: PRINT STATE">
	public void printState() {
		Trace.psLog(0, "[PRINT STATE START]");
		printFragmentManagers(1);
		printFragments(1);
		printViewGroupHierarchy(1);
		Trace.psLog(0, "[PRINT STATE END]");
	}
	private void printFragmentManagers(int icnt) {
		Trace.psLog(icnt, "FragmentManagers");
		icnt++;
		Trace.psLog(icnt, String.format("Transient  FMs: %s", getTransientMfs()));
		Trace.psLog(icnt, String.format("Persistent FMs: %s", MainActivity.getFragmentManagerMfs()));
	}
	private void printFragments(int icnt) {
		Trace.psLog(icnt, "Fragments");
		icnt++;
		printFragmentInfo(icnt, 1);
		printFragmentInfo(icnt, 2);
		printFragmentInfo(icnt, 3);
	}
	private void printViewGroupHierarchy(int icnt) {
		int cid = R.id.container_top;
		ViewGroup vg = (ViewGroup) findViewById(cid);
		if (vg == null) {
			String msg = String.format("No container for id: %#x!", cid);
			throw new IllegalStateException(msg);
		}
		Trace.printViewGroupHierarchy(icnt, vg);
	}
	private void printFragmentInfo(int icnt, int fno) {
		FragmentIdSet fid_set = FragmentIdSet.newInstanceFromFragmentNumber(fno);
		MyFragment mf = getMyFragment(fid_set, false, true);
		if (mf != null) {
			String s = mf.getData();
			Trace.psLog(icnt, s);
		}
	}
	//</editor-fold>
	//<editor-fold desc="METHODS: GET MY FRAGMENT">
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
		int buttonset_rid = ll.getId();
		FragmentIdSet fid_set = FragmentIdSet.newInstanceFromButtonsetRid(buttonset_rid);
		return getMyFragment(fid_set, create, false);
	}
	private MyFragment getMyFragmentFromNumber(int fno) {
		FragmentIdSet fid_set = FragmentIdSet.newInstanceFromFragmentNumber(fno);
		return getMyFragment(fid_set, false, false);
	}
	private MyFragment getMyFragment(FragmentIdSet fid_set, boolean create, boolean silent) {
		String ftag = fid_set.getFragmentTag();
		MyFragment mf = transientMyFragments.get(ftag);
		if (mf != null) {
			// This will only happen for dynamic fragments.
			gmfLog("getMyFragment()",
				String.format(
					"EXISTING fragment retrieved from transient storage: %s (%s).",
					mf.getMyTag(), Trace.classAtHc(mf)),
				silent);
			return mf;
		}

		FragmentManager fm = getFragmentManager();
		mf = (MyFragment) fm.findFragmentByTag(ftag);
		if (mf == null) {
			if (create) {
				// Only create dynamic fragments (static fragments are only created in XML).
				MyFragmentDynamic mfd = new MyFragmentDynamic();
				mfd.init(ftag, fid_set.getContainerRid());
				transientMyFragments.put(ftag, mfd);
				gmfLog("getMyFragment()",
					String.format(
						"NEW fragment created: %s (%s) ",
						mfd.getMyTag(), Trace.classAtHc(mfd)),
					silent);
				trace.logCode("mf = new MyFragmentDynamic();");

				setButtonLabelRetainInstance(
					"getMyFragment", fid_set.getFragmentNumber(), mfd.getRetainInstance());
				return mfd;
			}
			else
				gmfLog("getMyFragment()",
					String.format("No such fragment in FragmentManager %s.", ftag),
					silent);

		} else
			gmfLog("getMyFragment()",
				String.format("Existing fragment in FragmentManager: %s(%s)",
					mf.getMyTag(),
					Trace.classAtHc(mf)),
				silent);

		return mf;
	}
	private void gmfLog(String label, String msg, boolean silent) {
		if (!silent)
			trace.log(label, msg);
	}
	public String getTransientMfs() {
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
	static public String getFragmentManagerMfs() {
		// Unfortunately, we have to hardcode the list of fragments we are looking for.
		// Not sure how to probe the FragmentManager for the complete list of Fragments is has.
		StringBuffer sb = new StringBuffer();
		getFragmentManagerMf(sb, 1);
		getFragmentManagerMf(sb, 2);
		getFragmentManagerMf(sb, FRAGTAG_S1);
		return sb.toString();
	}
	static private void getFragmentManagerMf(StringBuffer sb, int fno) {
		FragmentIdSet fid_set = FragmentIdSet.newInstanceFromFragmentNumber(fno);
		getFragmentManagerMf(sb,fid_set.getFragmentTag());
	}
	static private void getFragmentManagerMf(StringBuffer sb, String tag) {
		Fragment f = fm.findFragmentByTag(tag);
		if (f != null){
			if (sb.length() > 0)
				sb.append(", ");
			sb.append(String.format("%s=%#x", tag, f.hashCode()));
		}
	}
	//</editor-fold>
}
//<editor-fold desc="CLASS: FragmentIdSet">
class FragmentIdSet
{
	// All four IDs are needed in different contexts at different times.
	//<editor-fold desc="FIELDS">
	private int    fragment_no;     // Simple id for the fragment.
	private int    buttonset_rid;   // Button set corresponding to a fragment.
	private int    container_rid;   // Container the fragment should be put into.
	private String fragment_tag;    // Tag for the fragment in the FragmentManager.
	//</editor-fold>
	//<editor-fold desc="CONSTRUCTORS">
	private FragmentIdSet(int fno, int brid, int crid, String ftag) {
		fragment_no   = fno;
		buttonset_rid = brid;
		container_rid = crid;
		fragment_tag  = ftag;
	}
	//</editor-fold>
	//<editor-fold desc="GETTERS">
	int getButtonSetRid()   { return buttonset_rid; }
	int getContainerRid()   { return container_rid; }
	int getFragmentNumber() { return fragment_no; }
	String getFragmentTag() { return fragment_tag; }
	//</editor-fold>
	//<editor-fold desc="METHODS: NEW INSTANCE">
	static FragmentIdSet newInstanceFromFragmentNumber(int fno) {
		return makeFragmentIdSet(fno);
	}
	static FragmentIdSet newInstanceFromButtonsetRid(int brid) {
		switch (brid) {
			case R.id.buttonset1: return makeFragmentIdSet(1);
			case R.id.buttonset2: return makeFragmentIdSet(2);
			case R.id.buttonset3: return makeFragmentIdSet(3);
			default:              throw new IllegalStateException("Bad button set rid: " + brid);
		}
	}
	static FragmentIdSet newInstanceFromContainerRid(int crid) {
		switch (crid) {
			case MainActivity.FRAGCONTAINER_D1: return makeFragmentIdSet(1);
			case MainActivity.FRAGCONTAINER_D2: return makeFragmentIdSet(2);
			case MainActivity.FRAGCONTAINER_S1: return makeFragmentIdSet(3);
			default: throw new IllegalStateException("Bad container rid: " + crid);
		}
	}
	static FragmentIdSet newInstanceFromFragmentTag(String tag) {
		switch (tag) {
			case MainActivity.FRAGTAG_D1:  return makeFragmentIdSet(1);
			case MainActivity.FRAGTAG_D2:  return makeFragmentIdSet(2);
			case MainActivity.FRAGTAG_S1:  return makeFragmentIdSet(3);
			default: throw new IllegalStateException("Bad fragment tag: " + tag);
		}
	}
	static private FragmentIdSet makeFragmentIdSet(int fno) {
		switch (fno) {
			case 1:  return new FragmentIdSet(1,
				R.id.buttonset1, MainActivity.FRAGCONTAINER_D1, MainActivity.FRAGTAG_D1);
			case 2:  return new FragmentIdSet(2,
				R.id.buttonset2, MainActivity.FRAGCONTAINER_D2, MainActivity.FRAGTAG_D2);
			case 3:  return new FragmentIdSet(3,
				R.id.buttonset3, MainActivity.FRAGCONTAINER_S1, MainActivity.FRAGTAG_S1);
			default: throw new IllegalStateException("Bad fragment number: " + fno);
		}
	}
	//</editor-fold>
}
//</editor-fold>
