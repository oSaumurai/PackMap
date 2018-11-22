package edu.osu.cse5236.group10.packmap;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AESCryptTest {

    private static final String TAG = "AESCryptTest";

    @Test
    public void encryptString() throws Exception {
        String toEncrypt = "t3st_string";
        String encrypted = AESCrypt.encrypt(toEncrypt);
        Log.d(TAG, "Encrypted String: " + encrypted);
        assertNotEquals(toEncrypt, encrypted);
    }

    @Test
    public void encryptThenDecryptString() throws Exception {
        String toEncrypt = "t3st_string";
        String encrypted = AESCrypt.encrypt(toEncrypt);
        String decrypted = AESCrypt.decrypt(encrypted);
        Log.d(TAG, "Encrypted String: " + encrypted);
        Log.d(TAG, "Decrypted String: " + decrypted);
        assertNotEquals(toEncrypt, encrypted);
        assertEquals(toEncrypt, decrypted);
    }
}
