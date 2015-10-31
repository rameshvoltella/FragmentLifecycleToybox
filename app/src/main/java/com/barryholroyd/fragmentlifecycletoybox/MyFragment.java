package com.barryholroyd.fragmentlifecycletoybox;

/**
 * Created by Barry on 10/31/2015.
 */
public abstract class MyFragment extends FragmentPrintStates
{
	abstract public String getData();
	abstract public String getMyTag();
	abstract public int getContainerId();
}
