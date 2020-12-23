package test.runner;

import java.awt.*;

class OptionGroup<T extends Enum<T> & Option> {
    private final CheckboxGroup checkboxGroup;
    private final T[] options;

    OptionGroup(CheckboxGroup checkboxGroup, T[] options) {
        this.checkboxGroup = checkboxGroup;
        this.options = options;
    }

    T getOption() {
        String name = checkboxGroup.getSelectedCheckbox().getLabel();
        for (T option : options) {
            if (option.getName().equals(name)) {
                return option;
            }
        }
        throw new AssertionError();
    }
}
