# TaskLogger


TaskLogger is a simple tools to track all the Activities in your application. The real-time activity status can be shown in the Logcat. The ONLY thing you need to do is add taskLogger to your project with minor modification in your AndroidManifest.xml file.

### Usage

* import the taskLogger project to your project
* change <application> name to "com.reginald.tasklogger.TaskLoggerApplication" and add TaskLoggerService in your AndroidManifest.xml
````xml

<application android:name="com.reginald.tasklogger.TaskLoggerApplication"
              ...............
              ...............
              >

              <service android:name="com.reginald.tasklogger.TaskLoggerService"
                       android:process=":taskLogger" />
                       
              ...............
              ...............
</application>

````

### Output
Use "$taskLogger" as filter in Logcat to see the output, which contains(see figure below):
* activity attributes(name, launchMode, intent flags, process info, other attributes defined in AndroidManifest)
* activity lifecycles(onCreate(),onResume, .......), and jumps between activities
* tasks and back stack status

