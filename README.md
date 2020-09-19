# For  QTI Bluetooth, you need :

repopick 37411 38107

## in manifest

```xml
<remove-project name="android_system_bt" />
<remove-project name="android_packages_apps_Bluetooth" />
<project path="system/bt" name="darkobas/android_system_bt" remote="github" revision="android-10-caf" />
<project path="packages/apps/Bluetooth" name="darkobas/android_packages_apps_Bluetooth" remote="github" revision="android-10-caf" />
<project path="vendor/qcom/opensource/commonsys-intf/bluetooth" name="micky387/android_vendor_qcom_opensource_bluetooth-commonsys-intf" remote="github" revision="android-10" />
<project path="vendor/qcom/opensource/commonsys/bluetooth_ext" name="micky387/android_vendor_qcom_opensource_bluetooth_ext" remote="github" revision="android-10" />
<project path="vendor/qcom/opensource/packages/apps/Bluetooth" name="micky387/android_vendor_qcom_opensource_packages_apps_Bluetooth" remote="github" revision="android-10"  />
<project path="vendor/qcom/opensource/commonsys/system/bt" name="micky387/android_vendor_qcom_opensource_system_bt" remote="github" revision="android-10" />
```

## This repo haved cloned from LA.UM.8.12.r1-14100-sm8250.0 CAF branch commits :

https://source.codeaurora.org/quic/la/platform/vendor/qcom-opensource/packages/apps/Bluetooth/commit/?h=LA.UM.8.12.r1-14100-sm8250.0&id=9ff55ee85eb9b572bd8c6fee2a64c03626f4d9b5
https://source.codeaurora.org/quic/la/platform/vendor/qcom-opensource/bluetooth-commonsys-intf/commit/?h=LA.UM.8.12.r1-14100-sm8250.0&id=e0c67fe2fe6dccd9d0d1a1a8b3cdd95c23ab58ce
https://source.codeaurora.org/quic/la/platform/vendor/qcom-opensource/bluetooth_ext/commit/?h=LA.UM.8.12.r1-14100-sm8250.0&id=711cdbf32c10cea2f223328aed96935bbb9f15cd
https://source.codeaurora.org/quic/la/platform/vendor/qcom-opensource/system/bt/commit/?h=LA.UM.8.12.r1-14100-sm8250.0&id=2d5960e8221d60e43b0a8863e1cb39a14cfbe3b8
