import groovy.lang.GroovyClassLoader;
import jdk.nashorn.api.scripting.URLReader;

import javax.script.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

public class RubyInterop {
    public static void main(String[] args) throws IOException, ScriptException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.setProperty("org.jruby.embed.localvariable.behavior", "transient");

        GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
        Class<?> greeterClass = groovyClassLoader.parseClass(new File("target/classes/Greeter.groovy"));

        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

        ScriptEngine jsEngine = scriptEngineManager.getEngineByExtension("js");
        FileReader jsReader = new FileReader(new File("target/classes/Greeter.js"));
        CompiledScript jsScript = ((Compilable) jsEngine).compile(jsReader);
        jsScript.eval();

        Object jsGreeter = ((Invocable)jsScript.getEngine()).getInterface(greeterClass);
        Method jsSayHello = jsGreeter.getClass().getDeclaredMethod("sayHello");
        jsSayHello.invoke(jsGreeter);

        ScriptEngine rubyEngine = scriptEngineManager.getEngineByExtension("rb");
        FileReader rubyReader = new FileReader(new File("target/classes/Greeter.rb"));
        CompiledScript rubyScript = ((Compilable) rubyEngine).compile(rubyReader);
        rubyScript.eval();

        Object rubyGreeter = ((Invocable)rubyScript.getEngine()).getInterface(greeterClass);
        Method rubySayHello = rubyGreeter.getClass().getDeclaredMethod("sayHello");
        rubySayHello.invoke(rubyGreeter);
    }
}
