package com.barryholroyd.fragment_lifecycle_toybox;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Barry on 10/13/2015.
 */

class Trace {
	//<editor-fold desc="FIELDS">
	static final public String LOGTAG_APPLC         = "FLD APP LC      ";
	static final public String LOGTAG_APP           = "FLD APP         ";
	static final public String LOGTAG_FRAGLC        = "FLD FRAG LC     ";    // tmp
	static final public String LOGTAG_FRAGLC_STAT   = "FLD FRAG LC STAT";
	static final public String LOGTAG_FRAGLC_DYN    = "FLD FRAG LC DYN ";
	static final public String LOGTAG_FRAG          = "FLD FRAG        ";    // tmp
	static final public String LOGTAG_FRAG_STAT     = "FLD FRAG STAT   ";
	static final public String LOGTAG_FRAG_DYN      = "FLD FRAG DYN    ";
	static final public String LOGTAG_VIEWGROUP     = "FLD VWGRP       ";
	static final public String LOGTAG_VIEW          = "FLD VIEW        ";
	static final public String LOGTAG_PRINT_STATE   = "FLD PRINT STATE ";

	static final public String SEP_APPLC		= "|";
	static final public String SEP_APP		    = "|";
	static final public String SEP_FRAGMENTLC	= "  |";
	static final public String SEP_FRAGMENT     = "  |";
	static final public String SEP_VIEWGROUP    = "    |";
	static final public String SEP_VIEW         = "    |";
	static final public String SEP_PRINT_STATE  = "STATE";

	static final public String INDENT_PRINT_STATE = "  ";

	static private Bhlogger    bhps = new Bhlogger(LOGTAG_PRINT_STATE);
	private Bhlogger    bh   = null;
	private Info        info = null;
	private String      sep  = null;

	static private Activity a         = null;
	static private TextView log_pane  = null;
	static private TextView code_pane = null;
	//</editor-fold>
	//<editor-fold desc="INTERFACES">
	interface Info {
		public String getData();
	}
	//</editor-fold>
	//<editor-fold desc="CONSTRUCTORS">
	Trace(String logtag, String _sep, Info _info) {
		bh     = new Bhlogger(logtag);
		sep    = _sep;
		info   = _info;
	}
	//</editor-fold>
	//<editor-fold desc="PUBLIC METHODS">
	static public void init(Activity activity) {
		a = activity;
		log_pane = (TextView) a.findViewById(R.id.log_pane);
		log_pane.setHorizontallyScrolling(true);
		log_pane.setMovementMethod(new ScrollingMovementMethod());
		initTypeface(a, log_pane);
		code_pane = (TextView) a.findViewById(R.id.code_pane);
		initTypeface(a, code_pane);
	}
	static public String classAtHc(Object obj) {
		if (obj == null)
			return "<null>";
		else
			return obj.getClass().getSimpleName() + '@' + getIdHc(obj);
	}
	static private String getIdHc(Object obj) {
		// Standardize on using hash codes for ids.
		if (obj == null)
			return "<null>";
		return String.format("%#x", obj.hashCode());
	}
	public void setLogTag(String s) {
		bh.setLogtag(s);
	}
	public void logCode(String text) {
		code_pane.setText(text);
	}

	public void log(boolean newline) {
		log("", "", newline);
	}

	public void log(String label, boolean newline) {
		log(label, "", newline);
	}
	public void log(String label) {
		log(label, "", false);
	}
	public void log(String label, String msg) {
		log(label, msg, false);
	}
	public void log(String label, String msg, boolean newline) {
		String data = "";
		if (info != null)
			data = info.getData();
		writelog(sep, label, data, msg, newline);
	}
	//</editor-fold>

	//<editor-fold desc="METHODS: PRINT">
	static public void printViewGroupHierarchy(int icnt, ViewGroup vg) {
		printView(icnt, "ViewGroup", vg);
		icnt++;
		int child_cnt = vg.getChildCount();
		for (int i = 0 ; i < child_cnt ; i++) {
			View v = vg.getChildAt(i);
			try {
				ViewGroup vg_new = (ViewGroup) v;
				printViewGroupHierarchy(icnt, vg_new);
			}
			catch (ClassCastException cce) {
				printView(icnt, "View", v);
			}
		}
	}
	static private void printView(int icnt, String view_type, View v) {
		String tag;
		Object obj = v.getTag();
		try {
			tag = (String) obj;
			if (tag == null)
				tag = "<null>";
		}
		catch (ClassCastException cce) {
			tag = "<null>";
		}
		String cahc = String.format("%-30s", Trace.classAtHc(v) + ',');
		String vtype = String.format("%-11s", view_type + ':');
		psLog(icnt,
			  String.format("%s getId=%#x C@HC=%s Tag=%s",
				vtype, v.getId(), cahc, tag));
	}

	static public void psLog(int icnt, String msg) {
		String indent_str = "";
		for (int i = 0 ; i < icnt ; i++)
			indent_str += INDENT_PRINT_STATE;
		writeline_ps(indent_str, msg);
	}
	//</editor-fold>

	//<editor-fold desc="PRIVATE METHODS">
	static private void initTypeface(Activity a, TextView tv) {
		Typeface myTypeface = Typeface.MONOSPACE;
		tv.setTypeface(myTypeface);
		tv.setTextSize(15);
	}
	private void writelog(String sep, String label, String data, String msg, boolean newline) {
		if (newline) {
			bh.log(" ");
			if (log_pane != null)
				log_pane.append("\n");
		}

		String leader = String.format("%s %s:", sep, label);
		if (data.equals("") || msg.equals("")) {
			writeline_log(bh, leader, String.format("Data:[%s] Msg:[%s]", data, msg));
		}
		else {
			writeline_log(bh, leader, String.format("Data:[%s]", data));
			writeline_log(bh, leader, String.format("Msg:[%s]", msg));
		}
	}
	static private void writeline_log(Bhlogger bh, String leader, String s) {
		String msg = String.format("%-33s %s", leader, s);
		writeline(bh, msg);
	}
	static private void writeline_ps(String indent, String msg) {
		String msg2 = String.format("%s: %s %s", SEP_PRINT_STATE, indent, msg);
		writeline(bhps, msg2);
	}
	static private void writeline(Bhlogger bh, String msg) {
		if (log_pane == null)
			msg += " Log Pane:[SKIPPING]";
		else
			log_pane.append(msg + '\n');
		bh.log(msg);
	}
	//</editor-fold>
}
