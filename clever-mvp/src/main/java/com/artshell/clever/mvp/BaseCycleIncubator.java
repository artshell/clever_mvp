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
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.OutsideLifecycleException;
import com.trello.rxlifecycle2.RxLifecycle;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Add reactive lifecycle's features for {@link BaseIncubator}
 *
 * @author artshell on 2018/9/8
 */
public class BaseCycleIncubator extends BaseIncubator implements LifecycleProvider<Lifecycle.Event> {

    private final Subject<Lifecycle.Event> incubatorCycle = PublishSubject.<Lifecycle.Event>create().toSerialized();

    /**
     * 无须调用此方法, 来至{@link DefaultLifecycleObserver}接口中的方法访问修饰符只能是public
     * 可以考虑继承{@link BaseIncubator}实现扩展需求
     * @param owner
     */
    @Override
    public final void onCreate(@NonNull LifecycleOwner owner) {
        super.onCreate(owner);
        incubatorCycle.onNext(Lifecycle.Event.ON_CREATE);
    }

    /**
     * 无须调用此方法, 来至{@link DefaultLifecycleObserver}接口中的方法访问修饰符只能是public
     * 可以考虑继承{@link BaseIncubator}实现扩展需求
     * @param owner
     */
    @Override
    public final void onStart(@NonNull LifecycleOwner owner) {
        super.onStart(owner);
        incubatorCycle.onNext(Lifecycle.Event.ON_START);
    }

    /**
     * 无须调用此方法, 来至{@link DefaultLifecycleObserver}接口中的方法访问修饰符只能是public
     * 可以考虑继承{@link BaseIncubator}实现扩展需求
     * @param owner
     */
    @Override
    public final void onResume(@NonNull LifecycleOwner owner) {
        super.onResume(owner);
        incubatorCycle.onNext(Lifecycle.Event.ON_RESUME);
    }

    /**
     * 无须调用此方法, 来至{@link DefaultLifecycleObserver}接口中的方法访问修饰符只能是public
     * 可以考虑继承{@link BaseIncubator}实现扩展需求
     * @param owner
     */
    @Override
    public final void onPause(@NonNull LifecycleOwner owner) {
        incubatorCycle.onNext(Lifecycle.Event.ON_PAUSE);
        super.onPause(owner);
    }

    /**
     * 无须调用此方法, 来至{@link DefaultLifecycleObserver}接口中的方法访问修饰符只能是public
     * 可以考虑继承{@link BaseIncubator}实现扩展需求
     * @param owner
     */
    @Override
    public final void onStop(@NonNull LifecycleOwner owner) {
        incubatorCycle.onNext(Lifecycle.Event.ON_STOP);
        super.onStop(owner);
    }

    /**
     * 无须调用此方法, 来至{@link DefaultLifecycleObserver}接口中的方法访问修饰符只能是public
     * 可以考虑继承{@link BaseIncubator}实现扩展需求
     * @param owner
     */
    @Override
    public final void onDestroy(@NonNull LifecycleOwner owner) {
        incubatorCycle.onNext(Lifecycle.Event.ON_DESTROY);
        super.onDestroy(owner);
    }

    @NonNull
    @CheckResult
    @Override
    public final Observable<Lifecycle.Event> lifecycle() {
        return incubatorCycle.hide();
    }

    @NonNull
    @CheckResult
    @Override
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull Lifecycle.Event event) {
        return RxLifecycle.bindUntilEvent(incubatorCycle, event);
    }

    @NonNull
    @CheckResult
    @Override
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycle.bind(incubatorCycle, INCUBATOR_LIFECYCLE);
    }

    private static final Function<Lifecycle.Event, Lifecycle.Event> INCUBATOR_LIFECYCLE = event -> {
        switch (event) {
            case ON_CREATE:
                return Lifecycle.Event.ON_DESTROY;
            case ON_START:
                return Lifecycle.Event.ON_STOP;
            case ON_RESUME:
                return Lifecycle.Event.ON_PAUSE;
            case ON_PAUSE:
                return Lifecycle.Event.ON_STOP;
            case ON_STOP:
                return Lifecycle.Event.ON_DESTROY;
            case ON_DESTROY:
                throw new OutsideLifecycleException("Cannot bind to LifecycleOwner's lifecycle when outside of it.");
            default:
                throw new UnsupportedOperationException("Binding to " + event + " not yet implemented");
        }
    };
}
