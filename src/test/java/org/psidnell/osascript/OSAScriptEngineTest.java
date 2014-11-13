package org.psidnell.osascript;

import static org.junit.Assert.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.Test;

public class OSAScriptEngineTest {

    private static final String EOL = System.getProperty("line.separator");

    @Test
    public void testApplescriptExecution () throws ScriptException {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngineFactory factory = new OSAScriptEngineFactory(OSAScriptEngineFactory.SCPT);
        engineManager.registerEngineName(factory.getEngineName(), factory);
        ScriptEngine engine = engineManager.getEngineByName(factory.getEngineName());
        assertEquals ("foo" + EOL, engine.eval("log \"foo\""));
    }
    
    @Test
    public void testJavascriptExecution () throws ScriptException {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngineFactory factory = new OSAScriptEngineFactory(OSAScriptEngineFactory.JS);
        engineManager.registerEngineName(factory.getEngineName(), factory);
        ScriptEngine engine = engineManager.getEngineByName(factory.getEngineName());
        assertEquals ("foo" + EOL, engine.eval("console.log ('foo')"));
    }    
}
