# FragmentLifecycleToybox
Provides a control panel to play with fragments (create, add to / remove from / replace in Fragment Manager, etc.).

Two fragments are maintained. You can see extensive tracing of lifecycle transitions (both activity and fragment) transpire as you take various actions. Output goes both to an in-app pane as well as to the log file.

This is very useful for someone who wants to understand fragment life cycles more deeply. A lot has been written about fragment transactions, but less has been written about what triggers the transition into various states. This helped me understand, for instance, when onCreateView() is called and when hence when new Views are generated.
