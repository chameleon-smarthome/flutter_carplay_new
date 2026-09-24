package com.oguzhnatly.flutter_android_auto

import android.content.Intent
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import androidx.annotation.OptIn
import androidx.car.app.CarAppService
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.Session
import androidx.car.app.annotations.ExperimentalCarApi
import androidx.car.app.model.Action
import androidx.car.app.model.CarIcon
import androidx.car.app.model.CarLocation
import androidx.car.app.model.ItemList
import androidx.car.app.model.ListTemplate
import androidx.car.app.model.Metadata
import androidx.car.app.model.PlaceListMapTemplate
import androidx.car.app.model.PlaceMarker
import androidx.car.app.model.Row
import androidx.car.app.model.Tab
import androidx.car.app.model.TabContents
import androidx.car.app.model.TabTemplate
import androidx.car.app.model.Template
import androidx.core.graphics.drawable.IconCompat

class MainScreen(carContext: CarContext) : Screen(carContext) {
    init {}

    override fun onGetTemplate(): Template {
        val appName =
            carContext.applicationInfo.loadLabel(carContext.packageManager)
                .toString() ?: ""

        return FlutterAndroidAutoPlugin.currentTemplate
            ?: buildPlaceholderTabTemplate(appName)
    }

    // Must be a TabTemplate, not a ListTemplate: the Car App Library locks in
    // whatever template TYPE this first call returns as the back-stack baseline
    // for this screen. Since the real content is always a TabTemplate, the very
    // first (pre-Dart) response has to be one too, or the first back-navigation
    // after that crashes with "BACK operation failed. Template types differ".
    private fun buildPlaceholderTabTemplate(appName: String): Template {
        val icon = CarIcon.Builder(
            IconCompat.createWithResource(carContext, carContext.applicationInfo.icon)
        ).build()

        val contentId = "faa_placeholder_tab"

        val loadingContent = ListTemplate.Builder()
            .setTitle(appName)
            .setLoading(true)
            .build()

        val tab1 = Tab.Builder()
            .setTitle(appName)
            .setIcon(icon)
            .setContentId(contentId)
            .build()

        val tab2 = Tab.Builder()
            .setTitle(" ")
            .setIcon(icon)
            .setContentId("faa_placeholder_tab_2")
            .build()

        return TabTemplate.Builder(object : TabTemplate.TabCallback {
            override fun onTabSelected(tabContentId: String) {}
        })
            .setHeaderAction(Action.APP_ICON)
            .setActiveTabContentId(contentId)
            .addTab(tab1)
            .addTab(tab2)
            .setTabContents(TabContents.Builder(loadingContent).build())
            .build()
    }
}
