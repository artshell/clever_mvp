/*
 * Copyright 2018 artshell. https://github.com/artshell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.artshell.clever.mvp;

import android.arch.lifecycle.DefaultLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

/**
 * @author artshell on 2018/9/8
 */
public class BaseIncubator implements DefaultLifecycleObserver {

    private Context context;
    private Bundle stateBundle;

    public final void attachLifecycle(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
    }

    public final void detachLifecycle(Lifecycle lifecycle) {
        lifecycle.removeObserver(this);
    }

    public final void onAppContext(Context appContext) {
        context = appContext;
    }

    public final Context getAppContext() {
        return context;
    }

    public final Bundle getStateBundle() {
        return stateBundle == null
                ? stateBundle = new Bundle()
                : stateBundle;
    }

    @CallSuper
    public void onIncubatorCreated() {

    }

    @CallSuper
    public void onIncubatorDestroy() {
        if (stateBundle == null || stateBundle.isEmpty()) return;
        stateBundle.clear();
    }

    @CallSuper
    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {

    }

    @CallSuper
    @Override
    public void onStart(@NonNull LifecycleOwner owner) {

    }

    @CallSuper
    @Override
    public void onResume(@NonNull LifecycleOwner owner) {

    }

    @CallSuper
    @Override
    public void onPause(@NonNull LifecycleOwner owner) {

    }

    @CallSuper
    @Override
    public void onStop(@NonNull LifecycleOwner owner) {

    }

    @CallSuper
    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {

    }
}
