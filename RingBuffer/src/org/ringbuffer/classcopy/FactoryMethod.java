/*
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

package org.ringbuffer.classcopy;

import org.ringbuffer.lang.Lang;

import java.lang.reflect.Method;

class FactoryMethod<T> implements Invokable<T> {
    private final Method method;

    FactoryMethod(Method method) {
        this.method = method;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T call(Object... arguments) {
        try {
            return (T) method.invoke(arguments);
        } catch (ReflectiveOperationException e) {
            throw Lang.uncheck(e);
        }
    }
}
