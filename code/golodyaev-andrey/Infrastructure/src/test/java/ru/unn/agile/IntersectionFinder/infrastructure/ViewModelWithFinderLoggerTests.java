package ru.unn.agile.IntersectionFinder.infrastructure;

import ru.unn.agile.IntersectionFinder.viewmodel.ViewModel;
import ru.unn.agile.IntersectionFinder.viewmodel.ViewModelTests;

public class ViewModelWithFinderLoggerTests extends ViewModelTests {
    @Override
    public void setUp() {
        Logger logger = new Logger("./ViewModelWithLoggerTests.log");
        super.setViewModel(new ViewModel(logger));
    }
}
