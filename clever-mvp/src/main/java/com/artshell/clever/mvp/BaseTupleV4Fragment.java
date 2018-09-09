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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @see Tuple
 *
 * @author artshell on 2018/9/8
 */
public class BaseTupleV4Fragment<C extends BaseIncubator> extends BaseV4Fragment<C> {

    @CallSuper
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedState) {
        int layoutResId = getClass().getAnnotation(Tuple.class).layout();
        if (layoutResId != Tuple.LAYOUT_NOT_DEFINED) {
            return inflater.inflate(layoutResId, container, false);
        }
        return super.onCreateView(inflater, container, savedState);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final C initIncubator() {
        return (C) TupleHelper.createIncubator(getClass());
    }

}
