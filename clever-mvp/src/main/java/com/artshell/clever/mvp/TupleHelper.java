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

/**
 * @author artshell on 2018/9/8
 */
final class TupleHelper {

    static BaseIncubator createIncubator(Class<?> annotatedClass) {
        try {
            return annotatedClass.getAnnotation(Tuple.class).incubator().newInstance();
        } catch (InstantiationException e) {
            throw new CleverMvpException("Cannot create an instance of Tuple#incubator()", e);
        } catch (IllegalAccessException e) {
            throw new CleverMvpException("Cannot create an instance of Tuple#incubator()", e);
        }
    }
}
