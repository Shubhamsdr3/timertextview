# timertextview

I created this project when I was working on a feature of my company to show the timer while recording audio. 

Through this i also learned the android's basic blocks of [Handler](https://developer.android.com/reference/android/os/Handler#:~:text=A%20Handler%20allows%20you%20to,is%20bound%20to%20a%20Looper%20), [Looper](https://developer.android.com/reference/kotlin/android/os/Looper#:~:text=android.os.Looper,until%20the%20loop%20is%20stopped), [MessageQueue](https://developer.android.com/reference/android/os/MessageQueue). 

To use this library:
```
  //project level build.gradle file:
  allprojects {
    repositories {
        mavenLocal()
        maven { url 'https://maven.google.com' } 
        maven { url "https://jitpack.io" } // <-- add this!
    }
  }
  
  //app level build.gradle file include:
  implementation 'com.github.Shubhamsdr3:timertextview:0.1.5'
  
```

Latest release: [![](https://jitpack.io/v/Shubhamsdr3/timertextview.svg)](https://jitpack.io/#Shubhamsdr3/timertextview)
