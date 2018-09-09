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

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

/**
 * @see Tuple
 *
 * @author artshell on 2018/9/8
 */
public class BaseTupleActivity<C extends BaseIncubator> extends BaseActivity<C> {

    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        int layoutResId = getClass().getAnnotation(Tuple.class).layout();
        if (layoutResId != Tuple.LAYOUT_NOT_DEFINED) {
            setContentView(layoutResId);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final C initIncubator() {
        return (C) TupleHelper.createIncubator(getClass());
    }
}
