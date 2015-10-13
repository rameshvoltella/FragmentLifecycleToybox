package com.barryholroyd.fragmentlifedemo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;

public class MainActivity extends ActivityPrintStates
{
	//<editor-fold desc="FIELDS">
	static public Bhlogger bh = new Bhlogger("FLD APP");

	static final public String SEP_APS  = "| ";
	static final public String SEP_APP  = "  | ";
	static final public String SEP_FRAG = "    | ";
	static final public String SEP_VGRP = "      | ";
	static final public String SEP_VIEW = "        | ";
	static final public String SEP_PRSTATE = "          ";

	private int button_parent_id = 0;
	private int button_id = 0;
	private int     frag_no             = 0;
	private int     frag_container_id   = 0;
	private String  frag_tag            = "FragTag0";

	private FragmentManager fm = getFragmentManager();
	private MyFragment mf  = null; // the current fragment of interest (mf1 or mf2)
	private MyFragment mf1 = null; // single hook, can hold a single instance
	private MyFragment mf2 = null; // single hook, can hold a single instance
	//</editor-fold>
	//<editor-fold desc="OVERRIDDEN METHODS">
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		trace("onCreate", String.format("%s: %#x", "R.id.buttonset1", R.id.buttonset1));
		trace("onCreate", String.format("%s: %#x", "R.id.buttonset2", R.id.buttonset2));
		setContentView(R.layout.activity_main);
	}
	//</editor-fold>
	//<editor-fold desc="BUTTONS">
	public void buttonCreateFragment(View v)	{
		getButtonInfo(v);
		tracesep("buttonCreateFragment");
		mf = MyFragment.newInstance(this);
		switch (frag_no) {
			case 1: mf1 = mf; break;
			case 2: mf2 = mf; break;
			default: throw new IllegalStateException("Bad fragment number: " + frag_no);
		}
		if (! checkVals()) return;
		printState();
	}
	public void buttonAddFragmentWithView(View v)		{
		getButtonInfo(v);
		String msg = String.format("tag: %s, container id: %#x", frag_tag, frag_container_id);
		trace("buttonAddFragmentWithView", msg);
		if (! checkVals()) return;
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(frag_container_id, mf, frag_tag);
		ft.commit();
	}
	public void buttonAddFragmentWithoutView(View v)		{
		getButtonInfo(v);
		trace("buttonAddFragmentWithoutView");
		if (! checkVals()) return;
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(mf, frag_tag);
		ft.commit();
	}
	public void buttonAddFragmentView(View v)		{
		getButtonInfo(v);
		trace("buttonAddFragmentView");
		if (! checkVals()) return;
		MyFragment mf = (MyFragment) fm.findFragmentByTag(frag_tag);
		View view = mf.getView();
		if (view == null) {
			trace("buttonAddFragmentView", "Fragment does not have a view.");
			return;
		}
		FldLinearLayout fld_ll = (FldLinearLayout) findViewById(frag_container_id);
		fld_ll.addView(view);
	}
	public void buttonRemoveFragmentView(View v)		{
		getButtonInfo(v);
		trace("buttonRemoveFragmentView");
		if (! checkVals()) return;
		MyFragment mf = (MyFragment) fm.findFragmentByTag(frag_tag);
		View view = mf.getView();
		if (view == null) {
			trace("buttonRemoveFragmentView", "Fragment does not have a view.");
			return;
		}
		FldLinearLayout fld_ll = (FldLinearLayout) findViewById(frag_container_id);
		fld_ll.removeView(view);
	}
	public void buttonRemoveFragment(View v)	{
		getButtonInfo(v);
		trace("buttonRemoveFragment");
		if (! checkVals()) return;
		FragmentTransaction ft = fm.beginTransaction();
		ft.remove(mf);
		ft.commit();
	}
	public void buttonReplaceFragment(View v)	{
		getButtonInfo(v);
		tracesep("buttonReplaceFragment");
		if (! checkVals()) return;
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(frag_container_id, mf, frag_tag);
		ft.commit();
	}
	public void buttonAttachFragment(View v)	{
		// From: http://developer.android.com/reference/
		//       android/app/FragmentTransaction.html#attach
		//       (android.app.Fragment)
		// Re-attach a fragment after it had previously been
		//   detached from the UI with detach(Fragment). This causes
		//   its view hierarchy to be re-created, attached to the UI,
		//   and displayed.
		getButtonInfo(v);
		tracesep("buttonAttachFragment");
		if (! checkVals()) return;
		FragmentTransaction ft = fm.beginTransaction();
		ft.attach(mf);
		ft.commit();
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
		getButtonInfo(v);
		tracesep("buttonDetachFragment");
		if (! checkVals()) return;
		FragmentTransaction ft = fm.beginTransaction();
		ft.detach(mf);
		ft.commit();
	}
	public void buttonHideFragment(View v)		{
		// This will cause the fragment's View to be hidden.
		getButtonInfo(v);
		tracesep("buttonHideFragment");
		if (! checkVals()) return;
		FragmentTransaction ft = fm.beginTransaction();
		ft.hide(mf);
		ft.commit();
	}
	public void buttonShowFragment(View v)		{
		// This will cause the fragment's View to be shown.
		getButtonInfo(v);
		tracesep("buttonShowFragment");
		if (! checkVals()) return;
		FragmentTransaction ft = fm.beginTransaction();
		ft.show(mf);
		ft.commit();
	}
	//</editor-fold>
	//<editor-fold desc="SUPPORT METHODS">
	private boolean checkVals() {
		if (mf == null) {
			bh.log("*** MyFragment is null... skipping command.");
			return false;
		}

		return true;
	}
	private void getButtonInfo(View v) {

		Button b		= (Button) v;
		button_id = b.getId();

		ViewParent vp		= b.getParent();
		View parent_view	= (View) vp;
		button_parent_id = parent_view.getId();

		switch (button_parent_id) {
			case R.id.buttonset1:
				frag_no = 1;
				mf = mf1;
				frag_tag = "FragTag1";
				frag_container_id = R.id.container1;
				break;
			case R.id.buttonset2:
				frag_no = 2;
				mf = mf2;
				frag_tag = "FragTag2";
				frag_container_id = R.id.container2;
				break;
			default: throw new IllegalStateException("Bad fragment id: " + button_parent_id);
		}
	}
	//</editor-fold>
	//<editor-fold desc="PRINT METHODS">
	private void printState() {
		printFragmentInfo(1, "FragTag1");
		printFragmentInfo(2, "FragTag2");

		printContainerInfo(1, R.id.container1);
		printContainerInfo(2, R.id.container2);

	}
	private void printFragmentInfo(int frag_no, String frag_tag) {
		MyFragment mf = (MyFragment) fm.findFragmentByTag(frag_tag);
		if (mf == null) {
			String s = String.format("Fragment #%d: <null>",
				frag_no);
			trace("Fragment", s);
			return;
		}

		boolean	is_added	= mf.isAdded();
		boolean	is_detached	= mf.isDetached();
		int	frag_id		= mf.getId();
		FldTextView frag_view = (FldTextView) mf.getView();
		String	vstring	= frag_view.toStringSimple();
// Requires API 23
//		Object	host_obj	= mf.getHost();
//		String	host_obj_string	= host_obj == null
//					  ? "<null>"
//					  : host_obj.toString();
		String	tag		= mf.getTag();
		int	hash_code	= mf.hashCode();
		String s = String.format(
// API 23:   "Fragment #%d: %b,%b,%#x,%s,%s,%s,%#x",
			"Fragment #%d: %b,%b,%#x,%s,%s,%#x",
			frag_no,
			is_added,
			is_detached,
			frag_id,
			vstring,
// API 23:	host_obj_string,
			tag,
			hash_code
		);
		trace("Fragment", s);
	}
	private void printContainerInfo(int frag_no, int frag_container_id) {
		FldLinearLayout fld_ll = (FldLinearLayout) findViewById(frag_container_id);

		if (fld_ll == null) {
			throw new IllegalStateException(String.format(
				"Missing FldLinearLayout, id: %#x",
				frag_container_id));
		}

		// Print container info.
		String msg = String.format("Container #%d: ContainerId=%#x, ContainerHashCode=%#x",
			frag_no, frag_container_id, fld_ll.hashCode());
		String data = fld_ll.getFldLinearLayoutInfo();
		traceInfo("Container", data, msg);

		// Print container's children's info.
		int child_cnt = fld_ll.getChildCount();
		for (int i = 0 ; i < child_cnt ; i++) {
			// There should only be FldTextView children present.
			FldTextView fld_tv = (FldTextView) fld_ll.getChildAt(i);
			String msg2 = String.format("");
			String data2 = fld_tv.getFldTextViewInfo();
			traceInfo("FldTextView", data2, msg2);
		}
	}
	private void traceInfo(String label, String data, String msg) {
		traceMain(MainActivity.SEP_PRSTATE, label, data, msg);
	}

	private void tracesep(String label) {
		bh.log("-----------------------------\n");
		trace(label);
	}
	private void trace(String label) { trace(label, ""); }
	private void trace(String label, String msg) {
		String data = getAppInfo();
		traceMain(MainActivity.SEP_APP, label, data, msg);
	}
	private String getAppInfo() {
		String s = String.format(
			"FragNo=%d : This:%-22s :: Button:%#x -> ButtonParent:%#x",
			frag_no, this.toStringSimple(), button_id, button_parent_id);
		return s;
	}
	static public void traceMain(String sep, String label, String data, String msg) {
		String leader = String.format("%-20s [%s]", sep, label);
		String s = String.format("%-30s <%s> Msg: %s", leader, data, msg);
		bh.log(s);
	}
	public String toStringSimple() {
		return getClass().getSimpleName() + '@' + Integer.toHexString(hashCode());
	}
	//</editor-fold>
}



