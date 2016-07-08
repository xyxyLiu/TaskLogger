# TaskLogger

TaskLogger is a simple tool to track all the Activities within your application. The real-time activity status can be shown in the Logcat. The ONLY thing you need to do is to import taskLogger module in your project with minor modification in your AndroidManifest.xml file.


### Downloads
 - [android-tasklogger-1.1.jar](https://github.com/xyxyLiu/TaskLogger/releases/download/1.1/android-tasklogger-1.1.jar)

### Usage

* import the taskLogger project to your project
* change Application's name to "com.reginald.tasklogger.TaskLoggerApplication" and add TaskLoggerService in your AndroidManifest.xml.
````xml
<!-- your AndroidManifest.xml file -->

<!-- DO NOT FORGET TO ADD THIS PERMISSION -->
<uses-permission android:name="android.permission.GET_TASKS"/>

<application android:name="com.reginald.tasklogger.TaskLoggerApplication"
              ...............
              ...............
              >
              
              <!-- add tasklogger service in a separate process to trace the activity status -->
              <service android:name="com.reginald.tasklogger.TaskLoggerService"
                       android:process=":taskLogger" />
                       
              ...............
              ...............
</application>

````

If you implement your own Application in your project, use TaskLogger.init() in your Application.attachBaseContext(context):
````java
public class YourApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // added here!
        TaskLogger.init(this);
        ......
    }

    ......
}
````



* Please add the following line to your proguard file, if Proguard is enabled in your project.
````text
-keep public class com.reginald.tasklogger.TaskLoggerApplication$TaskLoggerInstrumentation { *; }
````

For more details, see the sample in the project.

### Output
Use "$taskLogger$" as filter in Logcat to see the output, which contains(see figure below):
* activity attributes(name, launchMode, intent flags, process info, other attributes defined in AndroidManifest)
* activity lifecycle callbacks (onCreate(),onResume(), ....... ), and jumps between activities(startActivity())
* tasks and back stack status

<img src="https://raw.githubusercontent.com/xyxyLiu/TaskLogger/master/output1.png" alt="Screenshot"/>


### Change log
* 1.0 first release
* 1.1 add TaskLogger.init()


### Apache License

Copyright 2015, Reginald Liu.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.


### Thanks
Direct-load-apk, http://finallody.github.io/Direct-Load-apk
