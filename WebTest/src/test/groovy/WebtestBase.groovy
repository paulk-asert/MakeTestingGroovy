/**
 * Recent versions of Webtest include a WebtestCase which incorporates
 * much of the functionality within this base script. However, that
 * test case class makes use of addShutdownHook which doesn't seem
 * to play nicely with gradle and the test case class makes it hard
 * to override that behavior without significant duplication.
 */
abstract class WebtestBase extends Script {
    private File base
    private String webtestHome
    def ant

    /**
     * workingDir might be different between gradle/IDE so normalize
     * by walking up parent folders until we find project base
     *
     * @param baseDirName
     */
    void setBaseDirName(String baseDirName) {
        base = new File(System.getProperty('user.dir'))
        while(base && base.name != baseDirName) {
            base = base.parentFile
        }
        assert base, "Base directory for project not found"
    }

    /**
     * Sets the relative path to the Webtest SDK before creating
     * an AntBuilder with some additional useful properties set.
     *
     * Note: Webtest is generally not available via maven so assume
     * we have it installed locally (usually within project)
     *
     * @param pathRelativeToBase
     */
    void setPathToWebtestSDK(String pathRelativeToBase) {
        assert base, "Required property 'base' not found, did you call 'setBaseDirName'?"
        webtestHome = "$base/$pathRelativeToBase"
        assert new File(webtestHome).exists(), "Webtest SDK not found"
        ant = new AntBuilder()
        // you'd set headless for a CI server
        //ant.project.setProperty('wt.headless', 'true')
        ant.project.setProperty('wt.generateDtd.skip', 'true')
        ant.project.setProperty('wt.generateDefinitions.skip', 'true')
        ant.project.setProperty('wt.config.resultpath', "$base/build/webtest-results")
        // to turn on extra ant debug logging
        //ant.project.buildListeners.firstElement().setMessageOutputLevel(3)
    }

    /**
     * Adds some additional task definitions and macros before invoking
     * the 'wt.before.testInWork' target as defined within webtest.xml.
     */
    void beforeWork() {
        assert ant, "Ant not set, did you run 'setPathToWebtestSDK'?"
        assert webtestHome, "Required property 'webtestHome' not found, did you call 'setPathToWebtestSDK'?"
        def f = new File("${webtestHome}/webtest.xml")
        ant.taskdef(resource: 'webtest.taskdef')
        ant.'import' (file: f)
        ant.project.executeTarget 'wt.before.testInWork'
    }

    /**
     * A wrapper around ant.webtest
     *
     * @param name of the webtest project
     * @param yield your test steps
     */
    void webtest(String name, Closure yield) {
        assert ant, "Ant not set, did you run 'setPathToWebtestSDK'?"
        ant.webtest(name: name, yield)
    }

    void afterWork() {
        assert ant, "Ant not set, did you run 'setPathToWebtestSDK'?"
        // we avoid addShutdownHook since if doesn't play nice with gradle
        sleep 2000
        ant.project.executeTarget 'wt.after.testInWork'
    }
}