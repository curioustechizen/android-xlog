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

