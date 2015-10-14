package com.barryholroyd.fragmentlifedemo;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

/**
 * Created by Barry on 10/13/2015.
 */
class Trace {
	static final public String SEP_APS = "|";
	static final public String SEP_APP = " |";
	static final public String SEP_FRG = "  |";
	static final public String SEP_VGP = "   |";
	static final public String SEP_VEW = "    |";

	private Bhlogger    bh   = null;
	private Info        info = null;
	private String      sep  = null;

	static private Activity a         = null;
	static private TextView log_pane  = null;

	interface Info {
		public String getData();
	}

	Trace(String logtag, String _sep, Info _info) {
		bh     = new Bhlogger(logtag);
		sep    = _sep;
		info   = _info;
	}

	static public void init(Activity activity) {
		a = activity;
		log_pane = (TextView) a.findViewById(R.id.log_pane);
//		DEL: log_pane.setMovementMethod(new ScrollingMovementMethod());
		initTypeface(a, log_pane);
	}

	static private void initTypeface(Activity a, TextView tv) {
//		Typeface myTypeface = Typeface.createFromAsset(a.getAssets(), "font/Roboto-BoldItalic.ttf");
		Typeface myTypeface = Typeface.MONOSPACE;
		tv.setTypeface(myTypeface);
		tv.setTextSize(15);
	}

	public void log(String label) { log(label, ""); }
	public void log(String label, String msg) {
		String data = info.getData();
		writelog(sep, label, data, msg);
	}
	// Main trace method. Used by all trace methods.
	private void writelog(String sep, String label, String data, String msg) {
		String leader = String.format("%s %s:", sep, label);
		String s = String.format("%-28s Data:[%s] Msg:[%s]", leader, data, msg);
		if (log_pane != null)
			log_pane.append(s + '\n');
		else
			bh.log("SKIPPING LOG PANE");
		bh.log(s);
	}
	static public String toStringSimple(Object obj) {
		if (obj == null)
			return "<null>";
		else
			return obj.getClass().getSimpleName() + '@' + Integer.toHexString(getId(obj));
	}
	static public int getId(Object obj) {
		return obj.hashCode();
	}
}
