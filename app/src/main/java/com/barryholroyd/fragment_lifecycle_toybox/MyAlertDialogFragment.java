package com.barryholroyd.fragment_lifecycle_toybox;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
//import android.support.v4.app.DialogFragment;

//<editor-fold desc="USAGE COMMENT">
/*
 * NOTES:
 *   o showMe() is overloaded. In both versions, any of the arguments can be null. If one
 *     of the buttons is null, it simply isn't displayed.
 *   o Providing an OnClickListener is not necessary if you don't need to handle button clicks.
 *
 *	Examples:
 *    // Vary the title and message.
 *    MyAlertDialogFragment.showMe(this, "Title 2", "Message 2", "Button 1", "Button 2", "Button 3");
 *    MyAlertDialogFragment.showMe(this, "Title 3 (no message)", null, "Button 1", "Button 2", "Button 3");
 *    MyAlertDialogFragment.showMe(this, null, "Message 4 (no title)", "Button 1", "Button 2", "Button 3");
 *    MyAlertDialogFragment.showMe(this, null, null, "Button 1", "Button 2", "Button 3");
 *
 *    // Show only selected buttons.
 *    MyAlertDialogFragment.showMe(this, "Show Selected Buttons", "Positive", "Positive", null, null);
 *    MyAlertDialogFragment.showMe(this, "Show Selected Buttons", "OK and Cancel", "OK", null, "Cancel");
 *    MyAlertDialogFragment.showMe(this, "Show Selected Buttons", "No Buttons", null, null, null);
 *
 *  DialogButton:
 *     // An implementation must be provided somewhere (in the app, here or a in separate Java class
 *     // file). However, the onClick() method can be empty if you don't need to handle button clicks.
 *	class DialogButton implements DialogInterface.OnClickListener {
 *		public void onClick(DialogInterface dialog, int whichButton) {
 *			String s = null;
 *			switch (whichButton) {
 *				case DialogInterface.BUTTON_POSITIVE: s = "OK";     break;
 *				case DialogInterface.BUTTON_NEUTRAL:  s = "EH";     break;
 *				case DialogInterface.BUTTON_NEGATIVE: s = "CANCEL"; break;
 *			}
 *			Log.d("MY_ALERT_DIALOG",
 *				String.format("Button clicked: %s", s));
 *		}
 *	}
 */
//</editor-fold>

/**
 * Created by Barry on 9/13/2015.
 *
 * This is a completely self-contained Alert Dialog implementation.
 *
 * The newInstance() approach is used because you shouldn't provide non-default
 * constructors for fragments. Instead of creating a new-default constructor and
 * passing in arguments, use newInstance() to create a new instance of the fragment
 * and use put*() and get*() methods to pass in the arguments.
 */
public class MyAlertDialogFragment extends DialogFragment
{
	//<editor-fold desc="FIELDS">
	static final String ARG_TITLE    = "title";
	static final String ARG_MESSAGE  = "message";
	static final String ARG_POSITIVE = "positive";
	static final String ARG_NEUTRAL  = "neutral";
	static final String ARG_NEGATIVE = "negative";
	//</editor-fold>
	//<editor-fold desc="OVERRIDES">
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String title    = getArguments().getString(ARG_TITLE);
		String message  = getArguments().getString(ARG_MESSAGE);
		String positive = getArguments().getString(ARG_POSITIVE);
		String neutral  = getArguments().getString(ARG_NEUTRAL);
		String negative = getArguments().getString(ARG_NEGATIVE);

		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());

		adb.setTitle(title);
		adb.setMessage(message);

		adb.setPositiveButton(positive, new DialogButton());
		adb.setNeutralButton(neutral, new DialogButton());
		adb.setNegativeButton(negative, new DialogButton());

		return adb.create();
	}
	//</editor-fold>
	//<editor-fold desc="STATIC METHODS">
	public static MyAlertDialogFragment newInstance(
		String title, String message, String positive, String neutral, String negative) {
		MyAlertDialogFragment fragment = new MyAlertDialogFragment();
		Bundle args = new Bundle();
		args.putString(ARG_TITLE,    title);
		args.putString(ARG_MESSAGE,  message);
		args.putString(ARG_POSITIVE, positive);
		args.putString(ARG_NEUTRAL,  neutral);
		args.putString(ARG_NEGATIVE, negative);
		fragment.setArguments(args);
		return fragment;
	}
	static public void showMe(Activity a,
	                          String title, String message, String positive,
	                          String neutral, String negative) {
		DialogFragment newFragment =
			MyAlertDialogFragment.newInstance(title, message, positive, neutral, negative);
		FragmentManager fm = a.getFragmentManager();

		try {
			newFragment.show(fm, "dialog_fragment");
		}
		catch (IllegalStateException e) {
			/*
			 * This can happen when an an Activity initializes a Bhlogger instance with
			 *   a reference to itself (e.g., bh.init(this)) and the Bhlogger instance
			 *   later tries to display a Dialog to the user, but after the Activity has
			 *   been stopped (e.g., it may have sent an intent and fired up another
			 *   Activity).
			 */
			Log.w("MY_ALERTDIALOG_FRAGMENT",
				"Caught IllegalStateException. " +
					"Probable cause: DialogFragment.show()called after saveOnInstanceState().");
			Toast.makeText(a, message, Toast.LENGTH_LONG).show();
		}
	}
	//</editor-fold>
}