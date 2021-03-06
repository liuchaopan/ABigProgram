package tech.soit.quiet.utils.component.persistence

import android.preference.PreferenceManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PreferenceTest {

    companion object {

        private const val TEST_VALUE = "modified"

    }

    private var init: String? by Preference.default()

    @Before
    fun setUp() {
        //clear default preferences
        PreferenceManager.getDefaultSharedPreferences(InstrumentationRegistry.getInstrumentation().targetContext).edit()
                .clear()
                .commit()


        init = TEST_VALUE
    }

    @Test
    fun modifyNull() {
        var name: String? by Preference.default()
        assertNull(name)
        name = TEST_VALUE
        assertEquals(name, TEST_VALUE)
    }

    @Test
    fun modifyExisted() {
        val init by Preference.default<String>()
        assertEquals(init, TEST_VALUE)
        this.init = "abc"
        assertEquals(init, "abc")
    }


}