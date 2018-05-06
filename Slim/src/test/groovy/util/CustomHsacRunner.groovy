package util

import nl.hsac.fitnesse.junit.HsacFitNesseRunner
import org.junit.runners.model.InitializationError

class CustomHsacRunner extends HsacFitNesseRunner {
    CustomHsacRunner(Class<?> suiteClass) throws InitializationError { super(suiteClass) }

    @Override
    protected String getFitNesseDir(Class<?> suiteClass) { "fitnesse" }

    @Override
    protected String getOutputDir(Class<?> klass) throws InitializationError { "build/results" }
}