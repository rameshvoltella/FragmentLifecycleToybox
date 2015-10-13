package com.barryholroyd.fragmentlifedemo;

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
		String leader = String.format("%s %s:", sep, label);
		String s = String.format("%-28s Data:[%s] Msg:[%s]", leader, data, msg);
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
