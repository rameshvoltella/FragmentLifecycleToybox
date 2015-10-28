## Synopsis
Provides a control panel to play with fragments (create, add to / remove from / replace in Fragment Manager, etc.).

Two fragments are maintained. You can see extensive tracing of lifecycle transitions (both activity and fragment) transpire as you take various actions. Output goes both to an in-app pane as well as to the log file.

This is very useful for someone who wants to understand fragment life cycles more deeply. A lot has been written about fragment transactions, but less has been written about what triggers the transition into various states. This helped me understand, for instance, when onCreateView() is called and hence when new Views are generated.

## Installation and Documentation
The installable APK binary and additional information including usage, feature set, architecture and implementation, are all available on the [wiki](https://github.com/barryholroyd/FragmentLifecycleToybox/wiki).

See also http://barryholroyd.com/android.

## Motivation
Fragment are arguably the most confusing aspect of Android development. There are, unfortunately, many reasons for this. As a result, I had what seemed like an ever-growing collection of questions that remained unanswered, even after posting them on various forums (such as Stack Overflow). I wrote this app so that I could carefully and precisely
control what happens to a fragment and observe the results as I did so. I learned a lot of interesting things, such as the following.

1. `fragment.isAdded()` does *not* mean that the fragment has been added to an Activity. Rather, it indicates
   whether or not the fragment is *attached* to an activity (`ft.detach()` will cause it to return false).
2. A call to a fragment's onDestroy() callback does *not* indicate that  the
   fragment is actually being destroyed -- only that it is being removed from
   the Activity (e.g., via a call to `fragmenttransaction.remove(fragment)`).
3. `fragmenttransaction.detach(fragment)` does not detach the fragment from the Activity. Instead, it will cause the fragment's View (if there is one) to be destroyed (and, as noted above, the fragment's isAdded() method to subsequently return false).
4. `fragment.isDetached()` returning *false* does not indicate that the fragment is attached. The method only returns *true* when `fragmenttransaction.detach(fragment)` has been called on the fragment so, for instance, if the fragment hasn't been added to an Activity yet then isDetached() will still return *false*. In other words, `! isDetached()` does not mean that the fragment is attached to an Activity.
5. `fragmenttransaction.attach()` will silently do a `fragmenttransaction.add()` if the fragment hasn't yet been added to the Activity. Superficially this might seem to be a good idea. However, it is undocumented and compounds the confusion around what *add* and *attach* mean when applied to fragments.

This information is pretty fundamental, yet isn't clearly documented or is incorrectly documents. E.g., the Fragment reference page has the following definition for `isAdded()`.

> Return true if the fragment is currently added to its activity.

In this app, you can see that this is not the case. As mentioned above, when isAdded() returns true it is an indication that the fragment has been *attached* to the app (and, hence, that its View exists, if the fragment's onCreateView() is coded to provide one).

There are also layers of confusion on top of this. E.g.:

1. When is fragment state retained and when isn't it? (See my [FragmentStorageDemo](https://github.com/barryholroyd/FragmentStorageDemo/blob/master/README.md) app for an exploration of fragment state retention across Activity instantiations.)
2. When is a fragment's associated View destroyed? And the related question, When is a fragment's onCreateView() called?
3. When is the *container* parameter to onCreateView() non-null?

I used this app to sort through all of the above and more. I hope others find it useful as well.

## License
See [License.txt](License.txt).
