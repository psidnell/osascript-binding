package org.psidnell.osascript;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

public class OSAScriptEngineFactory implements ScriptEngineFactory {

    public static final String JS = ".js";
    public static final String SCPT = ".scpt";
    
    private final Map<String, String> params = new HashMap<>();
    
    private final String defaultSuffix;
    
    public OSAScriptEngineFactory (String defaultSuffix) {
        this.defaultSuffix = defaultSuffix;
    }
    
    @Override
    public String getEngineName() {
        return OSAScriptEngine.class.getSimpleName();
    }

    @Override
    public String getEngineVersion() {
        return "1.0.0";
    }

    @Override
    public List<String> getExtensions() {
        return Arrays.asList(new String[]{SCPT, JS});
    }

    @Override
    public List<String> getMimeTypes() {
        return Arrays.asList(new String[]{"application/scpt"});
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList(new String[]{"osascript"});
    }

    @Override
    public String getLanguageName() {
        return "osascript";
    }

    @Override
    public String getLanguageVersion() {
        return "1.0.0";
    }

    @Override
    public Object getParameter(String key) {
        return params.get(key);
    }

    @Override
    public String getMethodCallSyntax(String obj, String m, String... args) {
        String methodCall = m + " ";
        for (String arg : args) {
            methodCall += arg + " ";
        }
        return methodCall;
    }

    @Override
    public String getOutputStatement(String toDisplay) {
        return "console.log(" + toDisplay + ");";
    }

    @Override
    public String getProgram(String... statements) {
        String program = "#!/usr/bin/osascript\n";
        for (String statement : statements) {
            program += statement + "\n";
        }
        return program;
    }

    @Override
    public ScriptEngine getScriptEngine() {
        return new OSAScriptEngine(this, defaultSuffix);
    }
}