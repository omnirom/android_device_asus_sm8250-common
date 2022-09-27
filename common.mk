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

# Overlays
DEVICE_PACKAGE_OVERLAYS += \
    $(LOCAL_PATH)/overlay \
    vendor/omni/overlay/CarrierConfig

PRODUCT_PACKAGES += \
    FrameworksResOverlay \
    SettingsProviderOverlay \
    SystemUIOverlay \
    TetheringConfigOverlay \
    WifiOverlay

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

$(call inherit-product, vendor/qcom/opensource/commonsys-intf/display/config/display-product-system.mk)
$(call inherit-product, vendor/qcom/opensource/commonsys/display/config/display-product-commonsys.mk)

# Exclude vibrator from InputManager
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/excluded-input-devices.xml:system/etc/excluded-input-devices.xml

# fastbootd
PRODUCT_PACKAGES += \
    android.hardware.fastboot@1.0-impl-mock \
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

# Init
PRODUCT_PACKAGES += \
    libinit_sm8250

# Live Wallpapers
PRODUCT_PACKAGES += \
    LiveWallpapers \
    LiveWallpapersPicker \
    VisualizationWallpapers \
    librs_jni

# NFC
PRODUCT_PACKAGES += \
    android.hardware.secure_element@1.2 \
    NfcNci \
    Tag \
    SecureElement \
    com.android.nfc_extras

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

# Soong namespaces
PRODUCT_SOONG_NAMESPACES += \
    $(LOCAL_PATH)

# SurfaceFlinger props
PRODUCT_DEFAULT_PROPERTY_OVERRIDES += \
    ro.surface_flinger.has_HDR_display=true \
    ro.surface_flinger.has_wide_color_display=true \
    ro.surface_flinger.use_color_management=true \
    ro.surface_flinger.wcg_composition_dataspace=143261696 \
    ro.surface_flinger.protected_contents=true \
    ro.surface_flinger.set_touch_timer_ms=200 \
    ro.surface_flinger.force_hwc_copy_for_virtual_displays=true

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

# Telephony extension
PRODUCT_PACKAGES += telephony-ext
PRODUCT_BOOT_JARS += telephony-ext

# Thermal
PRODUCT_PACKAGES += \
    android.hardware.thermal@2.0

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
    libnl

PRODUCT_BOOT_JARS += \
    WfdCommon
