package tk.zwander.oneuituner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class HexActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val result = Intent()
        result.putExtra("plugin", packageName)
        result.putExtra("plugin_name", getString(R.string.app_name))
        result.putExtra("REQUIRES_DEFAULT_ONLY_MODE", false)
        result.putExtra("REQUIRES_DAY_NIGHT_MODE", false)
        result.putExtra("HAS_NO_FRAMES_OPTION", false)
        result.putExtra("HAS_TINTABLE_TARGETS", false)
        result.putExtra("HAS_CUSTOM_PREVIEW_BG", false)

        setResult(Activity.RESULT_OK, result)
        finish()
    }
}