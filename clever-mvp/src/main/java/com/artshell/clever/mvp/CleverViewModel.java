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

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

/**
 * @author artshell on 2018/9/8
 */
public final class CleverViewModel<C extends BaseIncubator> extends AndroidViewModel {

    public CleverViewModel(@NonNull Application application) {
        super(application);
    }

    private C incubator;

    void setIncubator(C incubator) {
        if (this.incubator == null) {
            this.incubator = incubator;
            this.incubator.onAppContext(getApplication());
        }
    }

    C getIncubator() {
        return incubator;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        incubator.onIncubatorDestroy();
        incubator = null;
    }
}
