package com.barryholroyd.fragmentlifecycletoybox;

import android.animation.Animator;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/*
 * Usage:
 *   public class Fragment1 extends FragmentPrintStates { ... }
 */
public class FragmentPrintStates extends Fragment
{
  //<editor-fold desc="FIELDS">
	private Trace trace = new Trace(Trace.LOGTAG_FRAGLC, Trace.SEP_FRAGMENTLC, null);
  //</editor-fold>

  public void setLogTag(String s) {
	  trace.setLogTag(s);
  }

  //<editor-fold desc="OVERRIDES">
  // Called when the fragment's activity has been created and this fragment's view hierarchy instantiated.
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    runme("onActivityCreated()");
    super.onActivityCreated(savedInstanceState);
  }
  
  // Receive the result from a previous call to startActivityForResult(Intent, int).
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    runme("onActivityResult()");
    super.onActivityResult(requestCode, resultCode, data);
  }

  // Called when a fragment is first attached to its context.
  // API 23:
//  @Override
//  public void onAttach(Context context) {
//    runme("onAttach(c)");
//    super.onAttach(context);
//  }
  
  // This method was deprecated in API level 23. Use onAttach(Context) instead.
  @Override
  public void onAttach(Activity activity) {
    runme("onAttach(a)");
    super.onAttach(activity);
  }
  
  // This hook is called whenever an item in a context menu is selected.
  @Override
  public boolean onContextItemSelected(MenuItem item) {
    runme("onContextItemSelected()");
    return super.onContextItemSelected(item);
  }
  
  // Called to do initial creation of a fragment.
  @Override
  public void onCreate(Bundle savedInstanceState) {
    runme("onCreate()");
    super.onCreate(savedInstanceState);
  }
  
  // Called when a fragment loads an animation.
  @Override
  public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
    runme("onCreateAnimator()");
    return super.onCreateAnimator(transit, enter, nextAnim);
  }
  
  // Called when a context menu for the view is about to be shown.
  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    runme("onCreateContextMenu()");
    super.onCreateContextMenu(menu, v, menuInfo);
  }
  
  // Initialize the contents of the Activity's standard options menu.
  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    runme("onCreateOptionsMenu()");
    super.onCreateOptionsMenu(menu, inflater);
  }
  
  // Called to have the fragment instantiate its user interface view.
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    runme("onCreateView()");
    return super.onCreateView(inflater, container, savedInstanceState);
  }
  
  // Called when the fragment is no longer in use.
  @Override
  public void onDestroy() {
    runme("onDestroy()");
    super.onDestroy();
  }
  
  // Called when this fragment's option menu items are no longer being included in the overall options menu.
  @Override
  public void onDestroyOptionsMenu() {
    runme("onDestroyOptionsMenu()");
    super.onDestroyOptionsMenu();
  }
  
  // Called when the view previously created by onCreateView(LayoutInflater, ViewGroup, Bundle) has been detached from the fragment.
  @Override
  public void onDestroyView() {
    runme("onDestroyView()");
    super.onDestroyView();
  }
  
  // Called when the fragment is no longer attached to its activity.
  @Override
  public void onDetach() {
    runme("onDetach()");
    super.onDetach();
  }
  
  // Called when the hidden state (as returned by isHidden() of the fragment has changed.
  @Override
  public void onHiddenChanged(boolean hidden) {
    runme("onHiddenChanged()");
    super.onHiddenChanged(hidden);
  }

  // Called when a fragment is being created as part of a view layout inflation, typically from setting the content view of an activity.
  // API 23:
//  @Override
//  public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
//    runme("onInflate()");
//    super.onInflate(context, attrs, savedInstanceState);
//  }
  
  // This method was deprecated in API level 12. Use onInflate(Context, AttributeSet, Bundle) instead.
  @Override
  public void onInflate(AttributeSet attrs, Bundle savedInstanceState) {
    runme("onInflate()");
    super.onInflate(attrs, savedInstanceState);
  }
  
  // This method was deprecated in API level 23. Use onInflate(Context, AttributeSet, Bundle) instead.
  @Override
  public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
    // Fragment's onInflate() just calls the onInflate() method just above.
//    runme("onInflate()");
    super.onInflate(activity, attrs, savedInstanceState);
  }
  
  // This is called when the overall system is running low on memory, and actively running processes should trim their memory usage.
  @Override
  public void onLowMemory() {
    runme("onLowMemory()");
    super.onLowMemory();
  }
  
  // This hook is called whenever an item in your options menu is selected.
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    runme("onOptionsItemSelected()");
    return super.onOptionsItemSelected(item);
  }
  
  // This hook is called whenever the options menu is being closed (either by the user canceling the menu with the back/menu button, or when an item is selected).
  @Override
  public void onOptionsMenuClosed(Menu menu) {
    runme("onOptionsMenuClosed()");
    super.onOptionsMenuClosed(menu);
  }
  
  // Called when the Fragment is no longer resumed.
  @Override
  public void onPause() {
    runme("onPause()");
    super.onPause();
  }
  
  // Prepare the Screen's standard options menu to be displayed.
  @Override
  public void onPrepareOptionsMenu(Menu menu) {
    runme("onPrepareOptionsMenu()");
    super.onPrepareOptionsMenu(menu);
  }

  // Callback for the result from requesting permissions.
  // API 23:
//  @Override
//  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//    runme("onRequestPermissionsResult()");
//    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//  }
  
  // Called when the fragment is visible to the user and actively running.
  @Override
  public void onResume() {
    runme("onResume()");
    super.onResume();
  }
  
  // Called to ask the fragment to save its current dynamic state, so it can later be reconstructed in a new instance of its process is restarted.
  @Override
  public void onSaveInstanceState(Bundle outState) {
    runme("onSaveInstanceState()");
    super.onSaveInstanceState(outState);
  }
  
  // Called when the Fragment is visible to the user.
  @Override
  public void onStart() {
    runme("onStart()");
    super.onStart();
  }
  
  // Called when the Fragment is no longer started.
  @Override
  public void onStop() {
    runme("onStop()");
    super.onStop();
  }
  
  // Called when the operating system has determined that it is a good time for a process to trim unneeded memory from its process.
  @Override
  public void onTrimMemory(int level) {
    runme("onTrimMemory()");
    super.onTrimMemory(level);
  }
  
  // Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned, but before any saved state has been restored in to the view.
  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    runme("onViewCreated()");
    super.onViewCreated(view, savedInstanceState);
  }
  
  // Called when all saved state has been restored into the view hierarchy of the fragment.
  @Override
  public void onViewStateRestored(Bundle savedInstanceState) {
    runme("onViewStateRestored()");
    super.onViewStateRestored(savedInstanceState);
  }
  //</editor-fold>
  //<editor-fold desc="SUPPORT METHODS">
  private void runme(String label) {
    // The class should be either MyFragmentDynamic or MyFragmentStatic.
    String tag = null;
    int id     = 0;
    try {
      MyFragmentDynamic mf = (MyFragmentDynamic) this;
      tag = mf.getMyTag();
      id  = mf.getId();
    }
    catch (ClassCastException cce1) {
      try {
        MyFragmentStatic mf = (MyFragmentStatic) this;
        tag = "<static fragment>";
      }
      catch (ClassCastException cce2) {
        trace.log(label, "BAD CLASS: " + Trace.classAtHc(this));
        return;
      }
    }

    View v = this.getView();
    String viewstr = v == null ? "<null>" : Trace.classAtHc(v);
    String msg = String.format(
        "Tag=%s getId=%#x C@HC=%s Retained=%b isAdded=%b ExplicitlyDetached=%b View=%s",
        tag, id, Trace.classAtHc(this),
        this.getRetainInstance(), this.isAdded(), this.isDetached(), viewstr
    );

    trace.log(label, msg);
  }
  //</editor-fold>
}
