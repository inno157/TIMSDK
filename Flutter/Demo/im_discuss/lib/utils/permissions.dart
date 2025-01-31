import 'package:discuss/utils/toast.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';

class Permissions {
  static const List<String> _names = <String>[
    '日历',
    '相机',
    '联系人',
    '位置',
    'locationAlways',
    'locationWhenInUse',
    'mediaLibrary',
    '麦克风',
    'phone',
    '相册',
    'photosAddOnly',
    'reminders',
    'sensors',
    'sms',
    'speech',
    '本地存储',
    'ignoreBatteryOptimizations',
    'notification',
    'access_media_location',
    'activity_recognition',
    'unknown',
    'bluetooth',
    'manageExternalStorage',
    'systemAlertWindow',
    'requestInstallPackages',
    'appTrackingTransparency',
    'criticalAlerts',
    'accessNotificationPolicy',
    'bluetoothScan',
    'bluetoothAdvertise',
    'bluetoothConnect',
  ];

  static Future<bool> checkPermission(BuildContext context, int value) async {
    if (await Permission.byValue(value).request().isGranted) {
      return true;
    }
    showPermissionConfirmDialog(context, value);
    return false;
  }

  static Future<bool?> showPermissionConfirmDialog(
      BuildContext context, value) {
    return showDialog<bool>(
      context: context,
      builder: (context) {
        return CupertinoAlertDialog(
          title: Text("“云通信 IM”想访问您的${_names[value]}"),
          content: const Text("我们需要您的同意才能获取信息"),
          actions: <Widget>[
            CupertinoDialogAction(
              child:
                  const Text("不允许", style: TextStyle(color: Color(0xFF147AFF))),
              onPressed: () => Navigator.of(context).pop(), // 关闭对话框
            ),
            CupertinoDialogAction(
              child:
                  const Text("好", style: TextStyle(color: Color(0xFF147AFF))),
              onPressed: () {
                //关闭对话框并返回true
                Navigator.of(context).pop();
                openAppSettings();
              },
            ),
          ],
        );
      },
    );
  }
}
