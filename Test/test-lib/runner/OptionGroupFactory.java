package test.runner;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class OptionGroupFactory {
    private final List<Panel> panels = new ArrayList<>();

    <T extends Enum<T> & Option> OptionGroup<T> create(T[] options) {
        CheckboxGroup group = new CheckboxGroup();
        Panel panel = new Panel();
        boolean first = true;
        for (T option : options) {
            Checkbox checkbox = new Checkbox(option.getName(), group, first);
            first = false;
            panel.add(checkbox);
        }
        panels.add(panel);
        return new OptionGroup<>(group, options);
    }

    int getCreatedCount() {
        return panels.size();
    }

    void addTo(Container container) {
        for (Panel panel : panels) {
            container.add(panel);
        }
    }
}
