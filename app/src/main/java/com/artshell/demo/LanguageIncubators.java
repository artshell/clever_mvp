package com.artshell.demo;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;

import com.artshell.clever.mvp.BaseCycleIncubator;

import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author artshell on 2018/9/9
 */
public class LanguageIncubators extends BaseCycleIncubator {

    @SuppressLint("CheckResult")
    public void getItems(
            Consumer<List<String>> listConsumer,
            Consumer<Throwable> thrConsumer,
            Consumer<Subscription> subConsumer,
            Action actionFinally) {

        Flowable.just(Arrays.asList("Java", "C#", "JavaScript", "Rust"))
                .delaySubscription(100, TimeUnit.MILLISECONDS, Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(Lifecycle.Event.ON_STOP)) /* important */
                .onTerminateDetach()
                .doOnSubscribe(subConsumer)
                .doFinally(actionFinally)
                .subscribe(listConsumer, thrConsumer);
    }

}
