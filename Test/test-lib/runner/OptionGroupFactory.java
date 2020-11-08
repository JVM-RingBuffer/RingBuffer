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

package test.runner;

import java.awt.*;

class OptionGroupFactory {
    private final Container container;

    OptionGroupFactory(Container container) {
        this.container = container;
    }

    <T extends Enum<T> & Option> OptionGroup<T> create(T[] options) {
        CheckboxGroup group = new CheckboxGroup();
        Panel panel = new Panel();
        boolean first = true;
        for (T option : options) {
            Checkbox checkbox = new Checkbox(option.getName(), group, first);
            first = false;
            panel.add(checkbox);
        }
        container.add(panel);
        return new OptionGroup<>(group, options);
    }
}
