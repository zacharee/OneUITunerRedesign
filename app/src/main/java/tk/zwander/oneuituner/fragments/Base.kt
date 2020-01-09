package tk.zwander.oneuituner.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.recyclerview.widget.RecyclerView
import tk.zwander.oneuituner.R
import tk.zwander.oneuituner.util.pxToDp

abstract class Base : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    internal abstract val title: Int

    internal val keysToSync = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onResume() {
        super.onResume()

        activity?.setTitle(title)
        keysToSync.forEach { syncSummary(it) }
    }

    override fun onCreateRecyclerView(
        inflater: LayoutInflater?,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): RecyclerView {
        val recView = super.onCreateRecyclerView(inflater, parent, savedInstanceState)
        recView.clipToPadding = false
        recView.setPadding(0, 0, 0, run {
            val tv = TypedValue()
            context?.theme?.resolveAttribute(R.attr.actionBarSize, tv, true)
            context?.resources?.getDimensionPixelSize(tv.resourceId)!! + context!!.pxToDp(16f).toInt()
        })
        return recView
    }

    @CallSuper
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        syncSummary(key)
    }

    override fun onDestroy() {
        super.onDestroy()

        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    internal fun syncSummary(key: String?) {
        key?.let {
            if (keysToSync.contains(it)) {
                findPreference<Preference>(it)?.apply {
                    when {
                        this is ListPreference -> {
                            summary = entry
                        }
                        else -> {
                            summary = preferenceManager.sharedPreferences.all[it]?.toString()
                        }
                    }
                }
            }
        }
    }
}