package tk.zwander.oneuituner.util

import android.content.Context
import android.os.Build
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import javax.xml.parsers.DocumentBuilderFactory

/************** Status Bar **************/
fun Document.createDefaultClock(): Node {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement("com.android.systemui.statusbar.policy.QSClock")
        .apply {
            setAttribute("android:id", "@*com.android.systemui:id/clock")
            setAttribute("android:layout_width", "wrap_content")
            setAttribute("android:layout_height", "match_parent")
            setAttribute("android:gravity", "center")
            setAttribute("android:singleLine", "true")
            setAttribute("android:tag", if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) "status_bar_clock" else "@*com.android.systemui:string/qs_clock_tag_home_indicator")
            setAttribute("android:textAppearance", "@*com.android.systemui:style/TextAppearance.StatusBar.Clock")
        }, true)
}

fun Document.createAOSPClock(): Node {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement("com.android.systemui.statusbar.policy.Clock")
        .apply {
            setAttribute("android:gravity", "center")
            setAttribute("android:id", "@+id/clock_aosp")
            setAttribute("android:layout_height", "match_parent")
            setAttribute("android:layout_width", "wrap_content")
            setAttribute("android:minWidth", "10dp")
            setAttribute("android:singleLine", "true")
            setAttribute("android:tag", if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) "status_bar_clock" else "@*com.android.systemui:string/qs_clock_tag_home_indicator")
            setAttribute("android:textAppearance", "@*com.android.systemui:style/TextAppearance.StatusBar.Clock")
        }, true)
}

fun Document.createCustomClock(format: String): Node {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement("TextClock")
        .apply {
            setAttribute("android:gravity", "center")
            setAttribute("android:id", "@+id/clock_custom")
            setAttribute("android:layout_height", "match_parent")
            setAttribute("android:layout_width", "wrap_content")
            setAttribute("android:minWidth", "10dp")
            setAttribute("android:singleLine", "true")
            setAttribute("android:tag", if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) "status_bar_clock" else "@*com.android.systemui:string/qs_clock_tag_home_indicator")
            setAttribute("android:textAppearance", "@*com.android.systemui:style/TextAppearance.StatusBar.Clock")
            setAttribute("android:format12Hour", format)
            setAttribute("android:format24Hour", format)
            setAttribute("android:shadowColor", "#7f000000")
            setAttribute("android:shadowDx", "1")
            setAttribute("android:shadowDy", "1")
            setAttribute("android:shadowRadius", "4")
        }, true)
}

fun Document.createPhoneStatusBarView(): Node {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement("com.android.systemui.statusbar.phone.PhoneStatusBarView")
        .apply {
            setAttribute("xmlns:android", "http://schemas.android.com/apk/res/android")
            setAttribute("android:id", "@*com.android.systemui:id/status_bar")
            setAttribute("android:layout_width", "match_parent")
            setAttribute("android:layout_height", "@*com.android.systemui:dimen/status_bar_height")
            setAttribute("android:accessibilityPaneTitle", "@*com.android.systemui:string/status_bar")
            setAttribute("android:background", "@*com.android.systemui:drawable/system_bar_background")
            setAttribute("android:descendantFocusability", "afterDescendants")
            setAttribute("android:focusable", "false")
            setAttribute("android:orientation", "vertical")

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P)
                setAttribute("android:layoutDirection", "ltr")
        }, true)
}

fun Document.createPhoneStatusBarBackground(): Node {
    return importNode(
        DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .newDocument()
            .createElement("FrameLayout")
            .apply {
                setAttribute("android:id", "@*com.android.systemui:id/${if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) "background" else "phone_status_bar_background"}")
                setAttribute("android:layout_width", "match_parent")
                setAttribute("android:layout_height", "match_parent")
            }, true)
}

fun Document.createNotificationIconArea(): Node {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement("com.android.systemui.statusbar.AlphaOptimizedFrameLayout")
        .apply {
            setAttribute("android:id", "@*com.android.systemui:id/notification_icon_area")
            setAttribute("android:layout_width", "0dp")
            setAttribute("android:layout_height", "match_parent")
            setAttribute("android:layout_weight", "1")
            setAttribute("android:orientation", "horizontal")
        }, true)
}

fun Document.createMiddleClockContainer(fakeID: String? = null, zeroWidth: Boolean = false, isGone: Boolean = true): Node {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement("LinearLayout")
        .apply {
            setAttribute("android:id", if (fakeID != null) "@+id/$fakeID" else "@*com.android.systemui:id/middle_clock_container")
            setAttribute("android:layout_width", if (zeroWidth) "0dp" else "wrap_content")
            setAttribute("android:layout_height", "match_parent")
            setAttribute("android:layout_gravity", "center")
            setAttribute("android:gravity", "center")
            setAttribute("android:orientation", "horizontal")

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                if (!zeroWidth) {
                    setAttribute("android:layout_marginStart", "@*com.android.systemui:dimen/status_bar_left_clock_end_padding")
                    setAttribute("android:layout_marginEnd", "@*com.android.systemui:dimen/status_bar_left_clock_end_padding")
                }
                if (isGone) setAttribute("android:visibility", "gone")
            }
        }, true)
}

fun Document.createStatusBarArea(): Node {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement("FrameLayout")
        .apply {
            setAttribute("android:id", "@*com.android.systemui:id/status_bar_area")
            setAttribute("android:layout_width", "match_parent")
            setAttribute("android:layout_height", "match_parent")
        }, true)
}

fun Document.createNotificationLightsOut(): Node {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement("ImageView")
        .apply {
            setAttribute("android:id", "@*com.android.systemui:id/notification_lights_out")
            setAttribute("android:layout_width", "@*com.android.systemui:dimen/status_bar_icon_size")
            setAttribute("android:layout_height", "match_parent")
            setAttribute("android:paddingLeft", "@*com.android.systemui:dimen/status_bar_padding_start")
            setAttribute("android:paddingBottom", "2dp")
            setAttribute("android:scaleType", "center")
            setAttribute("android:src", "@*com.android.systemui:drawable/ic_sysbar_lights_out_dot_small")
            setAttribute("android:visibility", "gone")
        }, true)
}

fun Document.createStatusBarContents(): Node {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement("LinearLayout")
        .apply {
            setAttribute("android:id", "@*com.android.systemui:id/status_bar_contents")
            setAttribute("android:layout_width", "match_parent")
            setAttribute("android:layout_height", "match_parent")
            setAttribute("android:orientation", "horizontal")
            setAttribute("android:paddingStart", "@*com.android.systemui:dimen/status_bar_padding_start")
            setAttribute("android:paddingEnd", "@*com.android.systemui:dimen/status_bar_padding_end")

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                setAttribute("android:paddingTop", "@*com.android.systemui:dimen/status_bar_padding_top")
            }
        }, true)
}

fun Document.createOperatorName(hide: Boolean = false): Node {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement("ViewStub")
        .apply {
            setAttribute("android:id", "@*com.android.systemui:id/operator_name")
            setAttribute("android:layout_width", if (hide) "0dp" else "wrap_content")
            setAttribute("android:layout_height", "match_parent")
            setAttribute("android:layout", "@*com.android.systemui:layout/operator_name")
        }, true)
}

fun Document.createLeftSideContainer(): Node {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement(if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) "FrameLayout" else "com.android.systemui.statusbar.phone.IndicatorGardenMaxWidthFrameLayout")
        .apply {
            setAttribute("android:id", "@*com.android.systemui:id/status_bar_left_side_container")
            setAttribute("android:layout_width", if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) "0dp" else "match_parent")
            setAttribute("android:layout_height", "match_parent")

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                setAttribute("android:layout_weight", "1")
            }
        }, true)
}

fun Document.createHeadsUpLayoutInclude(): Node {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement("include")
        .apply {
            setAttribute("layout", "@*com.android.systemui:layout/heads_up_status_bar_layout")
        }, true)
}

fun Document.createStatusBarLeftSide(): Node {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement(if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) "LinearLayout" else "com.android.systemui.statusbar.phone.IndicatorGardenMaxWidthLinearLayout")
        .apply {
            setAttribute("android:id", "@*com.android.systemui:id/status_bar_left_side")
            setAttribute("android:layout_width", "match_parent")
            setAttribute("android:layout_height", "match_parent")
            setAttribute("android:clipChildren", "false")

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                setAttribute("android:gravity", "center_vertical")
            }
        }, true)
}

fun Document.createLeftClockContainer(fakeID: String? = null, zeroWidth: Boolean = false): Node {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement("LinearLayout")
        .apply {
            setAttribute("android:id", if (fakeID != null) "@+id/$fakeID" else "@*com.android.systemui:id/left_clock_container")
            setAttribute("android:layout_width", if (zeroWidth) "0dp" else "wrap_content")
            setAttribute("android:layout_height", "match_parent")
            setAttribute("android:gravity", if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) "center_vertical" else "center")
            setAttribute("android:orientation", "horizontal")

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                setAttribute("android:layout_marginStart", "@*com.android.systemui:dimen/status_bar_left_clock_starting_padding")
                setAttribute("android:layout_marginEnd", "@*com.android.systemui:dimen/status_bar_left_clock_end_padding")
            }
        }, true)
}

fun Document.createHomeCarrierInfo(hide: Boolean = false): Node {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement("LinearLayout")
        .apply {
            setAttribute("android:id", "@*com.android.systemui:id/home_carrier_information_container")
            setAttribute("android:layout_width", if (hide) "0dp" else "wrap_content")
            setAttribute("android:layout_height", "match_parent")
            setAttribute("android:gravity", "center")
            setAttribute("android:orientation", "horizontal")
        }, true)
}

fun Document.createNetworkLogoImage(): Node {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement("LinearLayout")
        .apply {
            setAttribute("android:id", "@*com.android.systemui:id/network_logo_image_container")
            setAttribute("android:layout_width", "wrap_content")
            setAttribute("android:layout_height", "match_parent")
            setAttribute("android:gravity", "center")
            setAttribute("android:orientation", "horizontal")
        }, true)
}

fun Document.createCutoutSpace(): Node {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement("android.widget.Space")
        .apply {
            setAttribute("android:id", "@*com.android.systemui:id/cutout_space_view")
            setAttribute("android:layout_width", "0dp")
            setAttribute("android:layout_height", "match_parent")
            setAttribute("android:gravity", "center")
        }, true)
}

fun Document.createSystemIconArea(): Node {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement(if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) "com.android.keyguard.AlphaOptimizedLinearLayout" else "com.android.systemui.statusbar.phone.IndicatorGardenMaxWidthLinearLayout")
        .apply {
            setAttribute("android:id", "@*com.android.systemui:id/system_icon_area")
            setAttribute("android:layout_width", "wrap_content")
            setAttribute("android:layout_height", "match_parent")
            setAttribute("android:gravity", "right|center_vertical|center_horizontal|center|end")
            setAttribute("android:orientation", "horizontal")
        }, true)
}

fun Document.createQsKnoxCustomStatusBar(): Node {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement("ViewStub")
        .apply {
            setAttribute("android:id", "@*com.android.systemui:id/qs_knox_custom_statusbar_viewstub")
            setAttribute("android:layout_width", "wrap_content")
            setAttribute("android:layout_height", "match_parent")
            setAttribute("android:layout", "@*com.android.systemui:layout/qs_knox_custom_statusbar_text")
        }, true)
}

fun Document.createSystemIconsInclude(): Node {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement("include")
        .apply {
            setAttribute("layout", "@*com.android.systemui:layout/system_icons")
        }, true)
}

fun Document.createRightClockContainer(fakeID: String? = null, zeroWidth: Boolean = false, isGone: Boolean = true): Node {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement("LinearLayout")
        .apply {
            setAttribute("android:id", if (fakeID != null) "@+id/$fakeID" else "@*com.android.systemui:id/right_clock_container")
            setAttribute("android:layout_width", if (zeroWidth) "0dp" else "wrap_content")
            setAttribute("android:layout_height", "match_parent")
            setAttribute("android:gravity", "center")
            setAttribute("android:orientation", "horizontal")

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                if (!zeroWidth) setAttribute("android:layout_marginStart", "@*com.android.systemui:dimen/status_bar_left_clock_end_padding")
                if (isGone) setAttribute("android:visibility", "gone")
            }
        }, true)
}

fun Document.createEmergencyCryptKeeperText(): Node {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement("ViewStub")
        .apply {
            setAttribute("android:id", "@*com.android.systemui:id/emergency_cryptkeeper_text")
            setAttribute("android:layout_width", "wrap_content")
            setAttribute("android:layout_height", "match_parent")
            setAttribute("android:layout", "@*com.android.systemui:layout/emergency_cryptkeeper_text")
        }, true)
}

fun Document.createStatusBarCenter(): Node {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement("com.android.systemui.statusbar.phone.IndicatorGardenMaxWidthLinearLayout")
        .apply {
            setAttribute("android:id", "@*com.android.systemui:id/status_bar_center_side")
            setAttribute("android:layout_width", "0dp")
            setAttribute("android:layout_height", "match_parent")
            setAttribute("android:layout_centerInParent", "true")
            setAttribute("android:layout_gravity", "center")
            setAttribute("android:gravity", "center")
            setAttribute("android:layout_weight", "1")
            setAttribute("android:orientation", "horizontal")
        }, true)
}

fun Document.createCenteredIconArea(): Node {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement("com.android.systemui.statusbar.AlphaOptimizedFrameLayout")
        .apply {
            setAttribute("android:id", "@*com.android.systemui:id/centered_icon_area")
            setAttribute("android:layout_width", "wrap_content")
            setAttribute("android:layout_height", "match_parent")
            setAttribute("android:clipChildren", "false")
            setAttribute("android:gravity", "center")
            setAttribute("android:orientation", "horizontal")
        }, true)
}

fun Document.createHorizontalLinearLayout(): Element {
    return importNode(DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement("LinearLayout")
        .apply {
            setAttribute("android:layout_width", "wrap_content")
            setAttribute("android:layout_height", "match_parent")
            setAttribute("android:orientation", "horizontal")
        }, true) as Element
}

fun Context.makeAndroid10StatusBar(): Document {
    val clockType = prefs.clockType
    val leftSystemIcons = prefs.leftSystemIcons
    val clockPosition = prefs.clockPosition
    return DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .apply {
            appendChild(createPhoneStatusBarView())
                .apply {
                    appendChild(createPhoneStatusBarBackground())
                    appendChild(createStatusBarArea()).apply {
                        appendChild(createNotificationLightsOut())
                        appendChild(createStatusBarContents()).apply {
                            appendChild(createLeftSideContainer()).apply {
                                appendChild(createHeadsUpLayoutInclude())
                                appendChild(createStatusBarLeftSide()).apply {
                                    val hideCarrier = prefs.hideStatusBarCarrier
                                    appendChild(createOperatorName(hideCarrier))
                                    appendChild(createHomeCarrierInfo(hideCarrier))
                                    if (leftSystemIcons) {
                                        appendChild(createSystemIconArea()).apply {
                                            appendChild(createQsKnoxCustomStatusBar())
                                            appendChild(createSystemIconsInclude())
                                        }
                                    }
                                    appendChild(createLeftClockContainer(null, true)).apply {
                                        appendChild(createDefaultClock())
                                    }
                                    if (clockPosition == prefs.clockPositionLeft) {
                                        appendChild(createLeftClockContainer("fake_left_clock")).apply {
                                            appendChild(
                                                when (clockType) {
                                                    prefs.clockTypeAosp -> createAOSPClock()
                                                    prefs.clockTypeCustom -> createCustomClock(prefs.clockFormat)
                                                    else -> createDefaultClock()
                                                }
                                            )
                                        }
                                    }
                                    appendChild(createNotificationIconArea())
                                }
                            }
                            appendChild(createHorizontalLinearLayout().apply {
                                setAttribute("android:layout_width", "0dp")
                                setAttribute("android:layout_weight", "1")
                            })
                            appendChild(createStatusBarCenter()).apply {
                                appendChild(createCutoutSpace())
                                appendChild(createCenteredIconArea())
                                appendChild(createMiddleClockContainer(null, true))
                                if (clockPosition == prefs.clockPositionMiddle) {
                                    appendChild(createMiddleClockContainer("fake_middle_clock", isGone = false)).apply {
                                        appendChild(
                                            when (clockType) {
                                                prefs.clockTypeAosp -> createAOSPClock()
                                                prefs.clockTypeCustom -> createCustomClock(prefs.clockFormat)
                                                else -> createDefaultClock()
                                            }
                                        )
                                    }
                                }
                            }
                            appendChild(createHorizontalLinearLayout().apply {
                                setAttribute("android:layout_width", "0dp")
                                setAttribute("android:layout_weight", "1")
                            })
                            if (!leftSystemIcons) {
                                appendChild(createSystemIconArea()).apply {
                                    appendChild(createQsKnoxCustomStatusBar())
                                    appendChild(createSystemIconsInclude())
                                }
                            }
                            appendChild(createRightClockContainer())
                            if (clockPosition == prefs.clockPositionRight) {
                                appendChild(createRightClockContainer("fake_right_clock", isGone = false)).apply {
                                    appendChild(
                                        when (clockType) {
                                            prefs.clockTypeAosp -> createAOSPClock()
                                            prefs.clockTypeCustom -> createCustomClock(prefs.clockFormat)
                                            else -> createDefaultClock()
                                        }
                                    )
                                }
                            }
                        }
                        appendChild(createEmergencyCryptKeeperText())
                    }
                }
        }
}

fun Context.makeAndroid9StatusBar(): Document {
    val clockType = prefs.clockType
    val leftSystemIcons = prefs.leftSystemIcons
    return DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .apply {
            appendChild(createPhoneStatusBarView())
                .apply {
                    appendChild(createPhoneStatusBarBackground())
                    appendChild(createMiddleClockContainer())
                    appendChild(createStatusBarArea()).apply {
                        appendChild(createNotificationLightsOut())
                        appendChild(createStatusBarContents()).apply {
                            appendChild(createOperatorName(prefs.hideStatusBarCarrier))
                            appendChild(createLeftSideContainer()).apply {
                                appendChild(createHeadsUpLayoutInclude())
                                appendChild(createStatusBarLeftSide()).apply {
                                    if (leftSystemIcons) {
                                        appendChild(createSystemIconArea()).apply {
                                            appendChild(createQsKnoxCustomStatusBar())
                                            appendChild(createSystemIconsInclude())
                                            appendChild(createRightClockContainer())
                                        }
                                    }
                                    appendChild(createLeftClockContainer()).apply {
                                        appendChild(createNetworkLogoImage())
                                        appendChild(
                                            when (clockType) {
                                                prefs.clockTypeAosp -> createAOSPClock()
                                                prefs.clockTypeCustom -> createCustomClock(prefs.clockFormat)
                                                else -> createDefaultClock()
                                            }
                                        )
                                    }
                                    appendChild(createNotificationIconArea())
                                }
                            }
                            appendChild(createCutoutSpace())
                            if (!leftSystemIcons) {
                                appendChild(createSystemIconArea()).apply {
                                    appendChild(createQsKnoxCustomStatusBar())
                                    appendChild(createSystemIconsInclude())
                                    appendChild(createRightClockContainer())
                                }
                            }
                        }
                        appendChild(createEmergencyCryptKeeperText())
                    }
                }
        }
}