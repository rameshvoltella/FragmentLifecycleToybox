package com.barryholroyd.fragmentlifecycletoybox;

import android.app.Activity;
import android.os.Bundle;

public class ActivityPrintStates extends Activity
{
  //<editor-fold desc="FIELDS">
  private Trace trace = new Trace(Trace.LOGTAG_APPLC, Trace.SEP_APPLC, null);
  //</editor-fold>
  //<editor-fold desc="OVERRIDDEN METHODS">
  // Called at the start of the full lifetime.
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // Initialize Activity and inflate the UI.
    runme("onCreate()");
    super.onCreate(savedInstanceState);
  }

  // Called after onCreate has finished, use to restore UI state
  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    // Restore UI state from the savedInstanceState.
    // This bundle has also been passed to onCreate.
    // Will only be called if the Activity has been 
    // killed by the system since it was last visible.
    runme("onRestoreInstanceState()");
    super.onRestoreInstanceState(savedInstanceState);
  }

  // Called before subsequent visible lifetimes
  // for an activity process.
  @Override
  protected void onRestart(){
    // Load changes knowing that the Activity has already
    // been visible within this process.
    runme("onRestart()");
    super.onRestart();
  }

  // Called at the start of the visible lifetime.
  @Override
  protected void onStart(){
    // Apply any required UI change now that the Activity is visible.
    runme("onStart()");
    super.onStart();
  }

  // Called at the start of the active lifetime.
  @Override
  protected void onResume(){
    // Resume any paused UI updates, threads, or processes required
    // by the Activity but suspended when it was inactive.
    runme("onResume()");
    super.onResume();
  }

  // Called to save UI state changes at the
  // end of the active lifecycle.
  @Override
  protected void onSaveInstanceState(Bundle savedInstanceState) {
    // Save UI state changes to the savedInstanceState.
    // This bundle will be passed to onCreate and 
    // onRestoreInstanceState if the process is
    // killed and restarted by the run time.
    runme("onSaveInstanceState()");
    super.onSaveInstanceState(savedInstanceState);
  }

  // Called at the end of the active lifetime.
  @Override
  protected void onPause(){
    // Suspend UI updates, threads, or CPU intensive processes
    // that don't need to be updated when the Activity isn't
    // the active foreground Activity.
    runme("onPause()");
    super.onPause();
  }

  // Called at the end of the visible lifetime.
  @Override
  protected void onStop(){
    // Suspend remaining UI updates, threads, or processing
    // that aren't required when the Activity isn't visible.
    // Persist all edits or state changes
    // as after this call the process is likely to be killed.
    runme("onStop()");
    super.onStop();
  }

  // Sometimes called at the end of the full lifetime.
  @Override
  protected void onDestroy(){
    // Clean up any resources including ending threads,
    // closing database connections etc.
    runme("onDestroy()");
    super.onDestroy();
  }
  //</editor-fold>
  //<editor-fold desc="SUPPORT METHODS">
  private void runme(String label) {
    String msg = String.format("C@HC=%s", Trace.classAtHc(this));
    trace.log(label, msg);
  }
  //</editor-fold>
}
