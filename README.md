# log-files

Copyright © 2019, Christopher Alan Mosher, Shelton, Connecticut, USA, <cmosher01@gmail.com>.

[![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=CVSSQ2BWDCKQ2)
[![License](https://img.shields.io/github/license/cmosher01/log-files.svg)](https://www.gnu.org/licenses/gpl.html)

Java library to get a cross-platform log file.

# usage

```groovy
repositories {
    mavenCentral()
    maven {
        url = uri('https://public:\u0067hp_fya6Kseu3XOBMg2icbg7f1LP6ZFYjj35v4Zj@maven.pkg.github.com/cmosher01/*')
    }
}

dependencies {
    implementation group: 'nu.mine.mosher.io', name: 'log-files', version: 'latest.release'
}
```

```java
package com.example.foobar;
import nu.mine.mosher.io.LogFiles;

public class FooBar {
    public static void main(String[] args) {
        File logFile = LogFiles.getLogFileOf(FooBar.class);
        // ...
    }
}
```

# example with slf4j-simple

```groovy
dependencies {
    implementation group: 'nu.mine.mosher.io', name: 'log-files', version: 'latest.release'
    implementation group: 'org.slf4j', name: 'slf4j-api', version: 'latest.integration'
    runtimeOnly group: 'org.slf4j', name: 'slf4j-simple', version: 'latest.integration'
}
```

```java
package com.example.foobar;
import nu.mine.mosher.io.LogFiles;

public class FooBar {
    public static void main(String[] args) {
        File logFile = LogFiles.getLogFileOf(FooBar.class);
        System.out.println(logFile.getPath());

        System.setProperty("org.slf4j.simpleLogger.logFile", logFile.getPath());
        Logger log = LoggerFactory.getLogger(FooBar.class);
        log.info("Test logging message to log file.");
    }
}
```
