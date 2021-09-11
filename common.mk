# Copyright (C) 2016 The CyanogenMod Project
# Copyright (C) 2019 The OmniRom Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# Enable updating of APEXes
$(call inherit-product, $(SRC_TARGET_DIR)/product/updatable_apex.mk)

# Include GSI keys
$(call inherit-product, $(SRC_TARGET_DIR)/product/gsi_keys.mk)

# Overlays
DEVICE_PACKAGE_OVERLAYS += \
    $(LOCAL_PATH)/overlay \
    vendor/omni/overlay/CarrierConfig

# A/B
AB_OTA_UPDATER := true

AB_OTA_PARTITIONS += \
    boot \
    dtbo \
    odm \
    product \
    system \
    system_ext \
    vbmeta \
    vbmeta_system \
    vendor

AB_OTA_POSTINSTALL_CONFIG += \
    RUN_POSTINSTALL_system=true \
    POSTINSTALL_PATH_system=system/bin/omnipreopt_script \
    FILESYSTEM_TYPE_system=ext4 \
    POSTINSTALL_OPTIONAL_system=true

PRODUCT_PACKAGES += \
    omnipreopt_script

# ANT+
PRODUCT_PACKAGES += \
    AntHalService

# Bluetooth
PRODUCT_SOONG_NAMESPACES += vendor/qcom/opensource/commonsys/packages/apps/Bluetooth
PRODUCT_SOONG_NAMESPACES += vendor/qcom/opensource/commonsys/system/bt/conf

PRODUCT_PACKAGE_OVERLAYS += vendor/qcom/opensource/commonsys-intf/bluetooth/overlay/qva

PRODUCT_PACKAGES += BluetoothExt
PRODUCT_PACKAGES += libbluetooth_qti
PRODUCT_PACKAGES += vendor.qti.hardware.bluetooth_dun-V1.0-java

# Boot control
PRODUCT_PACKAGES += \
    android.hardware.boot@1.1-impl.recovery \
    bootctrl.kona.recovery

PRODUCT_PACKAGES_DEBUG += \
    bootctl

PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/fstab.qcom:$(TARGET_COPY_OUT_RAMDISK)/fstab.qcom

# Charger images
PRODUCT_PACKAGES += \
    omni_charger_res_images \
    animation.txt \
    font_charger.png

# DeviceParts
PRODUCT_PACKAGES += \
    DeviceParts \
    OmniDisplayManager

# Display
PRODUCT_PACKAGES += \
    libion \
    libtinyxml2

PRODUCT_PACKAGES += \
    libtinyalsa

# Exclude vibrator from InputManager
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/excluded-input-devices.xml:system/etc/excluded-input-devices.xml

# fastbootd
PRODUCT_PACKAGES += \
    fastbootd

# FM
PRODUCT_PACKAGES += \
    FM2 \
    libqcomfm_jni \
    qcom.fmradio

# HIDL
PRODUCT_PACKAGES += \
    android.hidl.base@1.0 \
    android.hidl.manager@1.0 \
    libhidltransport \
    libhwbinder

PRODUCT_BOOT_JARS += android.hidl.manager-V1.0-java

# Live Wallpapers
PRODUCT_PACKAGES += \
    LiveWallpapers \
    LiveWallpapersPicker \
    VisualizationWallpapers \
    librs_jni

# Prebuilt
PRODUCT_COPY_FILES += \
    $(call find-copy-subdir-files,*,device/asus/sm8250-common/prebuilt/product,product) \
    $(call find-copy-subdir-files,*,device/asus/sm8250-common/prebuilt/system,system) \
    $(call find-copy-subdir-files,*,device/asus/sm8250-common/prebuilt/system_ext,system_ext)

# Properties
BOARD_PROPERTY_OVERRIDES_SPLIT_ENABLED := true

# Netutils
PRODUCT_PACKAGES += \
    netutils-wrapper-1.0 \
    libandroid_net

PRODUCT_PACKAGES += \
    vndk_package

# Remove unwanted packages
PRODUCT_PACKAGES += \
    RemovePackages

# Soong namespaces
PRODUCT_SOONG_NAMESPACES += \
    $(LOCAL_PATH)

# Systemhelper
PRODUCT_PACKAGES += \
    vendor.qti.hardware.systemhelper@1.0

# Telephony
PRODUCT_PACKAGES += \
    ims-ext-common \
    ims_ext_common.xml \
    qti-telephony-hidl-wrapper \
    qti_telephony_hidl_wrapper.xml \
    qti-telephony-utils \
    qti_telephony_utils.xml \
    tcmiface

# Update engine
PRODUCT_PACKAGES += \
    otapreopt_script \
    update_engine \
    update_engine_sideload \
    update_verifier

PRODUCT_HOST_PACKAGES += \
    brillo_update_payload

PRODUCT_PACKAGES_DEBUG += \
    update_engine_client

PRODUCT_BUILD_SUPER_PARTITION := false
PRODUCT_USE_DYNAMIC_PARTITIONS := true

# Wifi
PRODUCT_PACKAGES += \
    libavservices_minijail \
    libnl \
    TetheringOverlay \
    WifiOverlay

PRODUCT_BOOT_JARS += \
    WfdCommon

include vendor/qcom/opensource/display-commonsys-intf/config/display-product-system.mk