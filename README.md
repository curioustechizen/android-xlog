android-xlog
============

Wrapper around `android.util.Log` that optionally logs to a file as well. This is useful as a debugging tool to get logs from testers.

**Not** meant as a replacement for more robust and sophisticated solutions like [ACRA](http://acra.ch/).

The goal is to have the **same API** as `android.util.Log` so that your existing code that uses `android.util.Log` can be made to log to a file by simply changing the imports.

There is one additional step required - initialize the Logger with the file to log to. This can be done once per app, for example in your sub-class of `android.app.Application`. 

Note that logging to a file is expensive and should only be present in special debug builds (not even in your regular debug builds - which might write only to LogCat). This is the reason an additional `boolean` has been provided in the `init` method.

###Usage

1. In your Application sub-class, do this:

```java
    private static final boolean LOG_TO_FILE = true;
    @Override
    public void onCreate(){
        File logFile = //the file to log to
        Log.init(this, LOG_TO_FILE, logFile);
    }
```

2. Change all your `android.util.Log` imports to `com.github.curioustechizen.xlog.Log`.
3. There is not Step 3!


See the example and the class documentation for `com.github.curioustechizen.xlog.Log` for more info on usage.


###Contributing

Contributions in the form of issues and pull requests are most welcome.


### FAQ

**What do I do with the log file?**

Well, that's up to you. Ideally, you'll have a button somewhere in your app that triggers an `ACTION_SEND` Intent to send the log file contents to you via e-mail etc. If the file is in external storage, you could just ask your testers to navigate to the file using a "standard" file browser and send you the file itself. But hey, you're not assuming that the user device has external storage, are you?

**What happens if the log file is not available?**

If the log file is not available (for example it is on external storage and said storage is mounted on a laptop etc) then the write will fail. You will see the message for this failure in logcat but obviously not in the log file. Note that in this we do not follow a fail-fast policy. Your app will continue to work since crashing when a log statement fails is a very very bad idea.

**Why is the log entry in the file not exactly the same as that in logcat?**

`android.util.Log` uses native code to actually write the final entry to logcat. This library, on the other hand, makes an approximation of that using a `StringBuilder`. I am guessing this serves most purposes.

**Why not simply use `Runtime` class and execute `logcat` command?**

Because using `logcat` command is not a part of the SDK. The behavior has [changed and broken apps](http://commonsware.com/blog/2012/07/12/read-logs-regression.html) in the past - there's no guarantee that this won't happen again.

**Why is logging to file expensive?**

Because every log statement is evaluated twice. What is logged to logcat is not the same string that is also inserted into a file. Instead, we prepare our own String to log into the file. This is, of course, in addition to the file IO related overhead.
