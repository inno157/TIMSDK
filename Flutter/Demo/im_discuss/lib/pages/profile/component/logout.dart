import 'package:discuss/utils/toast.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:tencent_im_sdk_plugin/models/v2_tim_callback.dart';
import 'package:tencent_im_sdk_plugin/tencent_im_sdk_plugin.dart';
import 'package:discuss/common/arrowright.dart';
import 'package:discuss/pages/login/login.dart';
import 'package:discuss/pages/profile/component/TextWithCommonStyle.dart';
import 'package:discuss/provider/conversion.dart';
import 'package:discuss/provider/friend.dart';
import 'package:discuss/provider/friendapplication.dart';
import 'package:discuss/provider/groupapplication.dart';
import 'package:discuss/provider/user.dart';

class Logout extends StatelessWidget {
  const Logout({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: () async {
        V2TimCallback res = await TencentImSDKPlugin.v2TIMManager.logout();
        // 去掉provider里面的所有东西

        if (res.code == 0) {
          try {
            Provider.of<ConversionModel>(context, listen: false).clear();
            Provider.of<UserModel>(context, listen: false).clear();
            // Provider.of<HistoryMessageListProvider>(context, listen: false)
            //     .clear();
            Provider.of<FriendListModel>(context, listen: false).clear();
            Provider.of<FriendApplicationModel>(context, listen: false).clear();
            Provider.of<GroupApplicationModel>(context, listen: false).clear();
            // 去掉存的一些数据
            Future<SharedPreferences> _prefs = SharedPreferences.getInstance();
            SharedPreferences prefs = await _prefs;
            prefs.remove('smsLoginUserId');
            prefs.remove('smsLoginToken');
            prefs.remove('smsLoginPhone');
          } catch (err) {
            Utils.log("someError");
            Utils.log(err);
          }
          Navigator.of(context).pushAndRemoveUntil(
            MaterialPageRoute(
                builder: (BuildContext context) => const LoginPage()),
            ModalRoute.withName('/'),
          );
        }
      },
      child: Container(
        height: 50,
        padding: const EdgeInsets.fromLTRB(16, 0, 16, 0),
        decoration: BoxDecoration(
          border: Border(
            bottom: BorderSide(
              color: Color(int.parse('ededed', radix: 16)).withAlpha(255),
              width: 1,
              style: BorderStyle.solid,
            ),
          ),
        ),
        child: Row(
          children: [
            const TextWithCommonStyle(
              "退出登录",
            ),
            Expanded(
              child: Row(
                textDirection: TextDirection.rtl,
                children: const [
                  ArrowRight(),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
