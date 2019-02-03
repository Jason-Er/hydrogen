package com.thumbstage.hydrogen;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.thumbstage.hydrogen", appContext.getPackageName());
    }

    @Test
    public void fetchLCConversation() {
        final List<String> convIdList = new ArrayList<>();
        AVQuery<AVObject> avQuery = new AVQuery<>("IStartedOpened");
        avQuery.orderByDescending("createdAt");
        // avQuery.include("conversation");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                if(avException == null) {
                    Log.i("fetchLCConversation", "fetch back size():" + avObjects.size());
                    for(AVObject avObject: avObjects) {
                        Log.i("fetchLCConversation", avObject.toString());
                        Log.i("fetchLCConversation", "name: ");
                        //convIdList.add(id);
                    }

                } else {
                    Log.i("fetchLCConversation", "Wrong: "+ avException.getMessage());
                }
            }
        });
    }
}
