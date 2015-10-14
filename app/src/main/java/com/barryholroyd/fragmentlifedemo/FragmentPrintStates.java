package com.barryholroyd.fragmentlifedemo;

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
  private Trace trace = new Trace(Trace.LOGTAG_FRAGMENTLC, Trace.SEP_FRAGMENTLC, null);
  //</editor-fold>
  //<editor-fold desc="OVERRIDES">
  // Called when the fragment's activity has been created and this fragment's view hierarchy instantiated.
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    runme("onActivityCreated()");
  }
  
  // Receive the result from a previous call to startActivityForResult(Intent, int).
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    runme("onActivityResult()");
  }

  // Called when a fragment is first attached to its context.
  // API 23:
//  @Override
//  public void onAttach(Context context) {
//    super.onAttach(context);
//    runme("onAttach(c)");
//  }
  
  // This method was deprecated in API level 23. Use onAttach(Context) instead.
  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    runme("onAttach(a)");
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
    super.onCreate(savedInstanceState);
    runme("onCreate()");
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
    super.onCreateContextMenu(menu, v, menuInfo);
    runme("onCreateContextMenu()");
  }
  
  // Initialize the contents of the Activity's standard options menu.
  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    runme("onCreateOptionsMenu()");
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
    super.onDestroy();
    runme("onDestroy()");
  }
  
  // Called when this fragment's option menu items are no longer being included in the overall options menu.
  @Override
  public void onDestroyOptionsMenu() {
    super.onDestroyOptionsMenu();
    runme("onDestroyOptionsMenu()");
  }
  
  // Called when the view previously created by onCreateView(LayoutInflater, ViewGroup, Bundle) has been detached from the fragment.
  @Override
  public void onDestroyView() {
    super.onDestroyView();
    runme("onDestroyView()");
  }
  
  // Called when the fragment is no longer attached to its activity.
  @Override
  public void onDetach() {
    super.onDetach();
    runme("onDetach()");
  }
  
  // Called when the hidden state (as returned by isHidden() of the fragment has changed.
  @Override
  public void onHiddenChanged(boolean hidden) {
    super.onHiddenChanged(hidden);
    runme("onHiddenChanged()");
  }

  // Called when a fragment is being created as part of a view layout inflation, typically from setting the content view of an activity.
  // API 23:
//  @Override
//  public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
//    super.onInflate(context, attrs, savedInstanceState);
//    runme("onInflate()");
//  }
  
  // This method was deprecated in API level 12. Use onInflate(Context, AttributeSet, Bundle) instead.
  @Override
  public void onInflate(AttributeSet attrs, Bundle savedInstanceState) {
    super.onInflate(attrs, savedInstanceState);
    runme("onInflate()");
  }
  
  // This method was deprecated in API level 23. Use onInflate(Context, AttributeSet, Bundle) instead.
  @Override
  public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
    super.onInflate(activity, attrs, savedInstanceState);
    runme("onInflate()");
  }
  
  // This is called when the overall system is running low on memory, and actively running processes should trim their memory usage.
  @Override
  public void onLowMemory() {
    super.onLowMemory();
    runme("onLowMemory()");
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
    super.onOptionsMenuClosed(menu);
    runme("onOptionsMenuClosed()");
  }
  
  // Called when the Fragment is no longer resumed.
  @Override
  public void onPause() {
    super.onPause();
    runme("onPause()");
  }
  
  // Prepare the Screen's standard options menu to be displayed.
  @Override
  public void onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    runme("onPrepareOptionsMenu()");
  }

  // Callback for the result from requesting permissions.
  // API 23:
//  @Override
//  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    runme("onRequestPermissionsResult()");
//  }
  
  // Called when the fragment is visible to the user and actively running.
  @Override
  public void onResume() {
    super.onResume();
    runme("onResume()");
  }
  
  // Called to ask the fragment to save its current dynamic state, so it can later be reconstructed in a new instance of its process is restarted.
  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    runme("onSaveInstanceState()");
  }
  
  // Called when the Fragment is visible to the user.
  @Override
  public void onStart() {
    super.onStart();
    runme("onStart()");
  }
  
  // Called when the Fragment is no longer started.
  @Override
  public void onStop() {
    super.onStop();
    runme("onStop()");
  }
  
  // Called when the operating system has determined that it is a good time for a process to trim unneeded memory from its process.
  @Override
  public void onTrimMemory(int level) {
    super.onTrimMemory(level);
    runme("onTrimMemory()");
  }
  
  // Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned, but before any saved state has been restored in to the view.
  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    runme("onViewCreated()");
  }
  
  // Called when all saved state has been restored into the view hierarchy of the fragment.
  @Override
  public void onViewStateRestored(Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
    runme("onViewStateRestored()");
  }
  //</editor-fold>
  //<editor-fold desc="SUPPORT METHODS">
  private void runme(String label) {
    trace.log(label);
  }
  //</editor-fold>
}