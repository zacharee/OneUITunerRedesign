package tk.zwander.oneuituner.util

import android.content.Context
import android.os.Build
import org.w3c.dom.Document
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory

fun Document.createKeyguardStatusBarView(): Element {
    return importElement(
        DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .newDocument()
            .createElement("com.android.systemui.statusbar.phone.KeyguardStatusBarView")
            .apply {
                setAttribute("xmlns:android", "http://schemas.android.com/apk/res/android")
                setAttribute("xmlns:systemui", "http://schemas.android.com/apk/res/com.android.systemui")
                setAttribute("android:id", "@*com.android.systemui:id/keyguard_header")
                setAttribute("android:layout_width", "match_parent")
                setAttribute(
                    "android:layout_height",
                    "@*com.android.systemui:dimen/status_bar_header_height_keyguard"
                )
                setAttribute("android:baselineAligned", "false")
            }
    )
}

fun Document.createKeyguardStatusBarArea(): Element {
    return importElement(
        DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .newDocument()
            .createElement("RelativeLayout")
            .apply {
                setAttribute("android:id", "@*com.android.systemui:id/keyguard_status_bar_area")
                setAttribute("android:layout_width", "match_parent")
                setAttribute("android:layout_height", "match_parent")
                setAttribute("android:gravity", "center_vertical")
            }
    )
}

fun Document.createStatusIconArea(): Element {
    return importElement(
        DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .newDocument()
            .createElement(if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) "com.android.systemui.statusbar.phone.IndicatorGardenMaxWidthLinearLayout" else "LinearLayout")
            .apply {
                setAttribute("android:id", "@*com.android.systemui:id/status_icon_area")
                setAttribute("android:layout_width", "wrap_content")
                setAttribute("android:layout_height", "match_parent")
                setAttribute("android:layout_alignParentEnd", "true")
                setAttribute("android:gravity", "right|center_vertical|center_horizontal|center|end")
            }
    )
}

fun Document.createSystemIconsContainer(): Element {
    return importElement(
        DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .newDocument()
            .createElement("FrameLayout")
            .apply {
                setAttribute("android:id", "@*com.android.systemui:id/system_icons_container")
                setAttribute("android:layout_width", if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) "0dp" else "wrap_content")
                setAttribute("android:layout_height", "match_parent")
                setAttribute("android:layout_marginStart", "@*com.android.systemui:dimen/system_icons_super_container_margin_start")
                setAttribute("android:gravity", "right|center_vertical|center_horizontal|center|end")
                setAttribute("android:paddingEnd", "@*com.android.systemui:dimen/system_icons_keyguard_padding_end")

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    setAttribute("android:layout_weight", "1")
                }
            }
    )
}

fun Document.createMultiUserSwitch(): Element {
    return importElement(
        DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .newDocument()
            .createElement("com.android.systemui.statusbar.phone.MultiUserSwitch")
            .apply {
                setAttribute("android:id", "@*com.android.systemui:id/multi_user_switch")
                setAttribute("android:layout_width", "@*com.android.systemui:dimen/multi_user_switch_width_keyguard")
                setAttribute("android:layout_height", "match_parent")
                setAttribute("android:layout_marginEnd", "@*com.android.systemui:dimen/multi_user_switch_keyguard_margin")
                setAttribute("android:background", "@*com.android.systemui:drawable/ripple_drawable")
            }
    )
}

fun Document.createMultiUserAvatar(): Element {
    return importElement(
        DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .newDocument()
            .createElement("ImageView")
            .apply {
                setAttribute("android:id", "@*com.android.systemui:id/multi_user_avatar")
                setAttribute("android:layout_width", "@*com.android.systemui:dimen/multi_user_avatar_keyguard_size")
                setAttribute("android:layout_height", "@*com.android.systemui:dimen/multi_user_avatar_keyguard_size")
                setAttribute("android:layout_gravity", "center")
                setAttribute("android:scaleType", "centerInside")
            }
    )
}

fun Document.createKeyguardLeftSideContainer(): Element {
    return importElement(
        DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .newDocument()
            .createElement("com.android.systemui.statusbar.phone.IndicatorGardenMaxWidthRelativeLayout")
            .apply {
                setAttribute("android:id", "@*com.android.systemui:id/keyguard_left_side_container")
                setAttribute("android:layout_width", "wrap_content")
                setAttribute("android:layout_height", "match_parent")
                setAttribute("android:layout_alignParentStart", "true")
                setAttribute("android:gravity", "center_vertical")
            }
    )
}

fun Document.createKeyguardNetworkInformationContainer(isVisibile: Boolean = true): Element {
    return importElement(
        DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .newDocument()
            .createElement(if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) "com.android.systemui.statusbar.phone.IndicatorGardenMaxWidthLinearLayout" else "LinearLayout")
            .apply {
                setAttribute("android:id", "@*com.android.systemui:id/keyguard_network_information_container")
                setAttribute("android:layout_width", if (isVisibile) "wrap_content" else "0dp")
                setAttribute("android:layout_height", "match_parent")
                setAttribute("android:layout_alignParentStart", "true")
                setAttribute("android:gravity", "center_vertical")
                setAttribute("android:orientation", "horizontal")

                if (!isVisibile) setAttribute("android:visibility", "gone")

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    setAttribute("android:paddingEnd", "@*com.android.systemui:dimen/carrier_information_margin_side")
                } else {
                    setAttribute("android:layout_marginStart", "@*com.android.systemui:dimen/keyguard_carrier_text_margin")
                    setAttribute("android:layout_marginEnd", "@*com.android.systemui:dimen/keyguard_operator_logo_margin_start")
                }
            }
    )
}

fun Document.createKeyguardCarrierText(isVisibile: Boolean = true): Element {
    return importElement(
        DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .newDocument()
            .createElement(if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) "com.android.keyguard.CarrierText" else "com.android.systemui.simpleindicator.SimpleIndicatorCarrierText")
            .apply {
                setAttribute("android:id", "@*com.android.systemui:id/keyguard_carrier_text")
                setAttribute("android:layout_width", "wrap_content")
                setAttribute("android:layout_height", "match_parent")
                setAttribute("android:ellipsize", "marquee")
                setAttribute("android:fontFamily", if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) "sec-roboto-regular" else "sec-roboto-condensed")
                setAttribute("android:gravity", "center_vertical")
                setAttribute("android:singleLine", "true")
                setAttribute("android:textColor", "?android:attr/textColorSecondary")
                setAttribute("android:textDirection", "locale")
                setAttribute("android:textSize", "@*com.android.systemui:dimen/status_bar_carrier_text_size")

                if (!isVisibile) setAttribute("android:visibility", "gone")

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    setAttribute("android:textAppearance", "@*com.android.systemui:style/TextAppearance.StatusBar.Clock")
//                    setAttribute("systemui:showAirplaneMode", "true")
//                    setAttribute("systemui:showMissingSim", "true")
                } else {
                    setAttribute("android:maxWidth", "@*com.android.systemui:dimen/carrier_label_portrait_max_width")
                    setAttribute("android:tag", "@*com.android.systemui:string/simple_indicator_tag_log_carrier_text_label")
                }
            }
    )
}

fun Document.createKeyguardClock(): Element {
    return importElement(
        DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .newDocument()
            .createElement("com.android.systemui.statusbar.policy.QSClock")
            .apply {
                setAttribute("android:id", "@*com.android.systemui:id/keyguard_clock")
                setAttribute("android:layout_width", "wrap_content")
                setAttribute("android:layout_height", "match_parent")
                setAttribute("android:layout_toEndOf", "@*com.android.systemui:id/keyguard_network_information_container")
                setAttribute("android:gravity", "left|center_vertical|center_horizontal|center|start")
                setAttribute("android:singleLine", "true")
                setAttribute("android:tag", "keyguard_status_bar_clock")
                setAttribute("android:textAppearance", "@*com.android.systemui:style/TextAppearance.StatusBar.Clock")

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    setAttribute("android:layout_marginEnd", "3dp")
                }
            }
    )
}

fun Context.makeAndroid10KeyguardStatusBar(): Document {
    return DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .apply {
            appendChild(createKeyguardStatusBarView().apply {
                appendChild(createKeyguardStatusBarArea().apply {
                    appendChild(createStatusIconArea().apply {
                        appendChild(createSystemIconsContainer().apply {
                            appendChild(createSystemIconsInclude())
                        })
                        appendChild(createMultiUserSwitch().apply {
                            appendChild(createMultiUserAvatar())
                        })
                    })
                    appendChild(createCutoutSpace())
                    appendChild(createKeyguardLeftSideContainer().apply {
                        appendChild(createKeyguardNetworkInformationContainer(!prefs.hideStatusBarCarrier).apply {
                            appendChild(createKeyguardCarrierText(!prefs.hideStatusBarCarrier))
                        })
                        appendChild(createKeyguardClock())
                    })
                })
            })
        }
}

fun Context.makeAndroid9KeyguardStatusBar(): Document {
    return DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .apply {
            appendChild(createKeyguardStatusBarView().apply {
                appendChild(createKeyguardStatusBarArea().apply {
                    appendChild(createStatusIconArea().apply {
                        appendChild(createSystemIconsContainer().apply {
                            appendChild(createSystemIconsInclude())
                        })
                        appendChild(createMultiUserSwitch().apply {
                            appendChild(createMultiUserAvatar())
                        })
                    })
                    appendChild(createCutoutSpace())
                    appendChild(createKeyguardNetworkInformationContainer(!prefs.hideStatusBarCarrier).apply {
                        appendChild(createKeyguardCarrierText())
                    })
                    appendChild(createKeyguardClock())
                })
            })
        }
}