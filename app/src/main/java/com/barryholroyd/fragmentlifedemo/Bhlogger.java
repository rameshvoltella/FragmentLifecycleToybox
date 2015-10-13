package com.barryholroyd.fragmentlifedemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;

/**
 * Created by Barry on 8/14/2015.
 *
 * DEPENDS ON: MyAlertDialogFragment.java.
 */
public class Bhlogger
{
	String logtag;
	Activity a;
	public Bhlogger(String logtag) {
		this.logtag = logtag;
	}
	public void init(Activity a) {
		this.a = a;
	}
	public void setLogtag(String logtag) {
		this.logtag = logtag;
	}
	public void log (String msg) {
		Log.d(logtag, msg);
	}
	public void trace (String msg) {
		Log.d(logtag, "TRACE: " + msg);
	}
	public void exception(Throwable e) {
		validateActivity(a);

		String emsg = e.getMessage();
		String cmsg = "<null>";
		Throwable c = e.getCause();
		if (c != null)
			cmsg = c.getMessage();

		String estr = String.format("Exception: %s", emsg);
		String cstr = String.format("Cause: %s", cmsg);
		String sstr = getSuppressed(e);
		String stck = getStack(e);

		log(estr);
		log(cstr);
		log(sstr);
		log(stck);

		String msg = estr + "\n\n" + cstr + "\n\n" + sstr + "\n\n" + stck;
		MyAlertDialogFragment.showMe(a, "Internal Error", msg, "OK", null, null);
	}
	public void popup(String msg) {
		validateActivity(a);
		MyAlertDialogFragment.showMe(a, null, msg, "OK", null, null);
	}
	private void validateActivity(Activity a) {
		// Check for init() not having been called to initialize 'a'.
		if (a == null)
			throw new IllegalStateException(
				String.format("FATAL - %s: %s", logtag,
					"Bhlogger.init() needs to be called before an exception occurs"));
	}
	@TargetApi(19)
	private String getSuppressed(Throwable e) {
		String s = "";
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Throwable[] suppressed = e.getSuppressed(); // requires API level 19
			for (Throwable t : suppressed) {
				s += "\nSuppressed: " + t.getMessage();
			}
			if (s.equals(""))
				s = "Suppressed: <none>";
		}
		else
			s = "Suppressed: <not supported in this version of Android>";

		return s;
	}
	private String getStack(Throwable e) {
		StackTraceElement[] st = e.getStackTrace();

		String stck = "Stack Trace:";
		for (StackTraceElement ste : st) {
			stck += String.format("\n  %s[%d] : %s->%s()",
				ste.getFileName(),
				ste.getLineNumber(),
				ste.getClassName(),
				ste.getMethodName());
		}
		return stck;
	}
}

class DialogButton implements DialogInterface.OnClickListener
{
	public void onClick(DialogInterface dialog, int whichButton) { }
}
