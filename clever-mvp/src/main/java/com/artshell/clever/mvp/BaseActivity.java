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

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * @author artshell on 2018/9/8
 */
public abstract class BaseActivity<C extends BaseIncubator>
        extends AppCompatActivity
        implements LifecycleProvider<ActivityEvent> {

    protected C incubator;
    
    private final Subject<ActivityEvent> lifecycleEvent = PublishSubject.<ActivityEvent>create().toSerialized();

    @SuppressWarnings("unchecked")
    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        CleverViewModel<C> viewModel = ViewModelProviders.of(this).get(CleverViewModel.class);
        boolean isCreated = false;
        if (viewModel.getIncubator() == null) {
            viewModel.setIncubator(initIncubator());
            isCreated = true;
        }
        incubator = viewModel.getIncubator();
        incubator.attachLifecycle(getLifecycle());
        if (isCreated) {
            incubator.onIncubatorCreated();
        }
        lifecycleEvent.onNext(ActivityEvent.CREATE);
    }

    protected abstract C initIncubator();

    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();
        lifecycleEvent.onNext(ActivityEvent.START);
    }

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();
        lifecycleEvent.onNext(ActivityEvent.RESUME);
    }

    @CallSuper
    @Override
    protected void onPause() {
        lifecycleEvent.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @CallSuper
    @Override
    protected void onStop() {
        lifecycleEvent.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        lifecycleEvent.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
        incubator.detachLifecycle(getLifecycle());
        incubator = null;
    }

    /**
     * 拦截触摸事件判断是否需要隐藏软键盘
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View focusView = getCurrentFocus();
            if (isShouldHideKeyboard(focusView, ev)) {
                hideKeyboard(focusView.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 隐藏软键盘
     * @param token
     */
    protected void hideKeyboard(IBinder token) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比, 来判断是否隐藏键盘;
     * 当用户点击EditText时则不能隐藏
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v instanceof EditText) {
            int[] location = {0, 0};
            v.getLocationInWindow(location);
            int left = location[0],
                    top = location[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left
                    && event.getX() < right
                    && event.getY() > top
                    && event.getY() < bottom) {
                return false; /* 点击EditText的事件，忽略它 */
            } else {
                v.clearFocus();
                return true;
            }
        }

        // 如果焦点不是EditText则忽略, 这个发生在视图刚绘制完,
        // 第一个焦点不在EditText上, 和用户用轨迹球选择其他的焦点
        return false;
    }

    @NonNull
    @Override
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleEvent.hide();
    }

    @Override
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleEvent, event);
    }

    @Override
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleEvent);
    }

}
