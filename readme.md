# BroadcastReceiver

## 一、概述

- Android 应用与 Android 系统和其他 Android 应用之间可以相互收发广播消息，这与 发布-订阅 设计模式相似。这些广播会在所关注的事件发生时发送。举例来说，Android 系统会在发生各种系统事件时发送广播，例如系统启动或设备开始充电时。再比如，应用可以发送自定义广播来通知其他应用它们可能感兴趣的事件（例如，一些新数据已下载）。

- 应用可以注册接收特定的广播。广播发出后，系统会自动将广播传送给同意接收这种广播的应用。

- 广播（ Broadcast ）用于Android 组件之间的灵活通信，与Activity 的区别在于：
    - Activity 只能一对一通信： Broadcast 可以一对多，一人发送广播， 多人接收处理。
    - 对于发送者来说，广播不需要考虑接收者有没有在工作，接收者在工作就接收广播，不在工作就丢弃广播。
    - 对于接收者来说，会收到各式各样的广播，所以接收者要自行过滤符合条件的广播，才能进行解包处理。

- 与广播有关的方法主要有以下3 个。
    - sendBroadcast：发送广播。

    - registerReceiver：注册接收器，一般在onStart 或onResume 方法中注册。

    - unregisterReceiver： 注销接收器，一般在onStop 或onPause 方法中注销。


## 二、接收广播

应用可以通过两种方式接收广播：清单声明的接收器和上下文注册的接收器。

### 2.1 清单声明的接收器

如果在清单中声明广播接收器，系统会在广播发出后启动您的应用（如果应用尚未运行）。

> ⭐**注意**：如果的应用以 API 级别 26 或更高级别的平台版本为目标，则不能使用清单为隐式广播（没有明确针对的应用的广播）声明接收器，但一些 不受此限制 的隐式广播除外。在大多数情况下，您可以使用 调度作业 来代替。

如果在清单中声明广播接收器，需要执行以下步骤：

1. 在应用清单中指定 `receiver`元素。

   ```xml
       <receiver android:name=".MyBroadcastReceiver"  android:exported="true">
           <intent-filter>
               <action android:name="android.intent.action.BOOT_COMPLETED"/>
               <action android:name="android.intent.action.INPUT_METHOD_CHANGED" />
           </intent-filter>
       </receiver>
       
   ```

   Intent 过滤器指定您的接收器所订阅的广播操作。

2. 创建 `BroadcastReceiver` 子类并实现 `onReceive(Context, Intent)`。以下示例中的广播接收器会记录并显示广播的内容：

   ```java
       public class MyBroadcastReceiver extends BroadcastReceiver {
               private static final String TAG = "MyBroadcastReceiver";
               @Override
               public void onReceive(Context context, Intent intent) {
                   //StringBuilder sb = new StringBuilder();
                   //sb.append("Action: " + intent.getAction() + "\n");
                   //sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
                   //String log = sb.toString();
                   //Log.d(TAG, log);
                   //Toast.makeText(context, log, Toast.LENGTH_LONG).show();
                   // TODO: This method is called when the BroadcastReceiver is receiving
                   // an Intent broadcast.
           		Log.d(TAG, "onReceive: 接收到了消息,消息是：" + intent.getStringExtra("data"));
               }
           }
       
   ```

系统软件包管理器会在应用安装时注册接收器。然后，该接收器会成为应用的一个独立入口点，这意味着如果应用当前未运行，系统可以启动应用并发送广播。

系统会创建新的 `BroadcastReceiver` 组件对象来处理它接收到的每个广播。此对象仅在调用 `onReceive(Context, Intent)` 期间有效。一旦从此方法返回代码，系统便会认为该组件不再活跃。

### 2.2 上下文注册的接收器

要使用上下文注册接收器，请执行以下步骤：

1. 创建 `BroadcastReceiver` 的实例。



   ```java
   MyReceiver myReceiver = null;
   ```



2. 创建 `IntentFilter` 并调用 `registerReceiver(BroadcastReceiver, IntentFilter)` 来注册接收器：

   ```java
   // MyReceiver.ACTION = "work.icu007.learnbroadcatreceiver.intent.action.MyReceiver";
   myReceiver = new MyReceiver();
   registerReceiver(myReceiver,new IntentFilter(MyReceiver.ACTION));
   ```

   > ⭐**注意：**要注册本地广播，请调用 `LocalBroadcastManager.registerReceiver(BroadcastReceiver, IntentFilter)`。

   只要注册上下文有效，上下文注册的接收器就会接收广播。例如，如果您在 `Activity` 上下文中注册，只要 Activity 没有被销毁，您就会收到广播。如果您在应用上下文中注册，只要应用在运行，您就会收到广播。

3. 要停止接收广播，请调用 `unregisterReceiver(android.content.BroadcastReceiver)`。当您不再需要接收器或上下文不再有效时，请务必注销接收器。

   请注意注册和注销接收器的位置，比方说，如果您使用 Activity 上下文在 `onCreate(Bundle)` 中注册接收器，则应在 `onDestroy()` 中注销，以防接收器从 Activity 上下文中泄露出去。如果您在 `onResume()` 中注册接收器，则应在 `onPause()` 中注销，以防多次注册接收器（如果您不想在暂停时接收广播，这样可以减少不必要的系统开销）。请勿在 `onSaveInstanceState(Bundle)` 中注销，因为如果用户在历史记录堆栈中后退，则不会调用此方法。

## 三、发送广播

Android 为应用提供三种方式来发送广播：

- `sendOrderedBroadcast(Intent, String)` 方法一次向一个接收器发送广播。当接收器逐个顺序执行时，接收器可以向下传递结果，也可以完全中止广播，使其不再传递给其他接收器。接收器的运行顺序可以通过匹配的 intent-filter 的 android:priority 属性来控制；具有相同优先级的接收器将按随机顺序运行。
- `sendBroadcast(Intent)` 方法会按随机的顺序向所有接收器发送广播。这称为常规广播。这种方法效率更高，但也意味着接收器无法从其他接收器读取结果，无法传递从广播中收到的数据，也无法中止广播。
- `LocalBroadcastManager.sendBroadcast` 方法会将广播发送给与发送器位于同一应用中的接收器。如果您不需要跨应用发送广播，请使用本地广播。这种实现方法的效率更高（无需进行进程间通信），而且您无需担心其他应用在收发您的广播时带来的任何安全问题。

以下代码段展示了如何通过创建 Intent 并调用 `sendBroadcast(Intent)` 来发送广播。

```java
// MyReceiver.ACTION = "work.icu007.learnbroadcatreceiver.intent.action.MyReceiver";
Intent intent = new Intent(MyReceiver.ACTION);
intent.putExtra("data","this is Charlie_Liao's LearnBroadcastReceiver Demo");
sendBroadcast(intent);
```

广播消息封装在 `Intent` 对象中。Intent 的操作字符串必须提供应用的 Java 软件包名称语法，并唯一标识广播事件。您可以使用 `putExtra(String, Bundle)` 向 intent 附加其他信息。您也可以对 intent 调用 `setPackage(String)`，将广播限定到同一组织中的一组应用。

> ⭐**注意**：虽然 intent 既用于发送广播，也用于通过 `startActivity(Intent)` 启动 Activity，但这两种操作是完全无关的。广播接收器无法查看或捕获用于启动 Activity 的 intent；同样，当您广播 intent 时，也无法找到或启动 Activity。

