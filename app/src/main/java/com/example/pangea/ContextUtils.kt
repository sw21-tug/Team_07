package com.example.pangea

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.LocaleList
import java.util.*

class ContextUtils(base: Context?) : ContextWrapper(base) {

    fun updateLocale(context: Context?, localeToSwitchTo: Locale?): ContextWrapper? {
        var context = context
        val resources = context?.resources
        val configuration = resources?.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val localeList = LocaleList(localeToSwitchTo)
            LocaleList.setDefault(localeList)
            configuration?.setLocales(localeList)
        } else {
            configuration?.locale = localeToSwitchTo
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            context = context?.createConfigurationContext(configuration!!)
        } else {
            resources?.updateConfiguration(configuration, resources.displayMetrics)
        }
        return ContextUtils(context)
    }
}