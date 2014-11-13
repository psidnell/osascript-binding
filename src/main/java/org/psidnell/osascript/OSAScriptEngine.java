package org.psidnell.osascript;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

public class OSAScriptEngine extends AbstractScriptEngine {

    private static final String EOL = System.getProperty("line.separator");
    private String defaultSuffix;
    private ScriptEngineFactory factory;

    public OSAScriptEngine(ScriptEngineFactory factory, String defaultSuffix) {
        this.defaultSuffix = defaultSuffix;
        this.factory = factory;
    }

    @Override
    public Object eval(String script, ScriptContext context)
            throws ScriptException {
        try (StringReader reader = new StringReader(script)) {
            return eval (reader, context);
        }
    }

    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        File file = null;
        String result = null;
        try {
            file = copyToScriptFile(reader);
            result = execute (file);
                        
        } catch (IOException e) {
            throw new ScriptException(e);
        }
        finally {
            if (file != null) {
                file.delete();
            }
        }

        return result;
    }
    
    private String execute(File file) throws IOException {
        StringBuilder result = new StringBuilder();
        String[] cmd = { "osascript", file.getAbsolutePath() };
        Process process = Runtime.getRuntime().exec(cmd);

        BufferedReader bufferedReader = new BufferedReader(
          new InputStreamReader(process.getErrorStream()));
        String line = bufferedReader.readLine();
        while (line != null) {
          result.append (line);
          result.append(EOL);
          line = bufferedReader.readLine();
        }
        return result.toString();
    }

    public File copyToScriptFile(Reader reader) throws IOException {
        File file = File.createTempFile("script", defaultSuffix);
        file.deleteOnExit(); // precaution
        
        try (BufferedReader bufReader = new BufferedReader(reader)) {
            try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter (file)))) {
                String line = bufReader.readLine();
                while (line != null) {
                    out.println(line);
                    line = bufReader.readLine();
                }
            }
        }

        return file;
    }

    @Override
    public Bindings createBindings() {
        return new SimpleBindings();
    }

    @Override
    public ScriptEngineFactory getFactory() {
        return factory;
    }
}
