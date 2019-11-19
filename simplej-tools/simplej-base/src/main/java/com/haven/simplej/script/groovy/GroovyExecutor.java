package com.haven.simplej.script.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author haven.zhang on 2018/9/23.
 *         groovy执行器（java调用ｇｒｏｏｖｙ）
 */
@Slf4j
public class GroovyExecutor {
    /**
     * script缓存
     */
    private Map<String, GroovyFile> grvMap = new HashMap<>();

    private static GroovyExecutor EXECUTOR = new GroovyExecutor();

    private ScriptEngineManager factory = new ScriptEngineManager();

    private ScriptEngine engine = factory.getEngineByName("groovy");
    private GroovyShell shell = new GroovyShell();
    private GroovyScriptEngine gsen = null;

    public static GroovyExecutor getExecutor() {
        return EXECUTOR;
    }

    /**
     * 指定script文件目录
     * @param path
     * @throws IOException
     */
    public void initPath(String[] path) throws IOException {
        gsen = new GroovyScriptEngine(path);
    }

    /**
     * 执行script语句
     * 适用于执行一段表达式语句，比如：a+b / a==b
     * 该方法实现了缓存机制，确保同一个表达式只会编译一次，产生一个class
     *
     * @param scriptText
     * @param args
     * @param alias      需要保证alias别名唯一，否则很容易导致oom
     * @return
     */
    public Object runScript(String scriptText, Binding args, String alias) {
        Script script = null;
        String md5Hex = DigestUtils.md5Hex(scriptText);
        if (grvMap.containsKey(alias)) {
            if (!md5Hex.equals(grvMap.get(alias).getMd5())) {
                script = shell.parse(scriptText, generateScriptName(alias));
                grvMap.get(alias).setScript(script);
                grvMap.get(alias).setMd5(md5Hex);
            } else {
                script = grvMap.get(alias).getScript();
            }
        } else {
            script = shell.parse(scriptText, generateScriptName(alias));
            grvMap.put(alias, new GroovyFile(script, md5Hex));
        }

        script.setBinding(args);
        return script.run();
    }

    /**
     * 执行groovy script
     * 该方法可以执行script脚步中指定方法
     *
     * @param scriptText
     * @param methodName
     * @param binding
     * @return
     * @throws Exception
     */
    public Object runScriptFunction(String scriptText, String methodName, Map<String,Object> binding, Object[] args) throws Exception {
        //javax.script.Bindings
        final Bindings bindings = engine.createBindings();
        bindings.putAll(binding);
        engine.eval(scriptText, bindings);
        Object result = ((Invocable) engine).invokeFunction(methodName, args);
        return result;
    }

    /**
     * 执行groovy 类的静态方法
     * @param scriptText
     * @param methodName
     * @param binding
     * @return
     * @throws Exception
     */
    public Object invokeStaticMethod(String scriptText, String methodName, Map<String,Object> binding, Object[] args) throws Exception {
        //javax.script.Bindings
        final Bindings bindings = engine.createBindings();
        bindings.putAll(binding);
        Object value = engine.eval(scriptText, bindings);
        Object result = ((Invocable) engine).invokeMethod(value, methodName, args);
        return result;
    }

    protected String generateScriptName(String alias) {
        return "Script" + alias + ".groovy";
    }

    /**
     * 加载并编译groovy文件内容，然后将类实例化，并执行对象指定的方法
     *
     * @param filePath
     * @param methodName
     * @param args
     * @return
     */
    public Object invokeMethod(String filePath, String methodName, Object[] args) throws IllegalAccessException, InstantiationException {
        Class<?> cls = GroovyFactory.getFactory().getGroovyClass(filePath);
        GroovyObject groovyObject = (GroovyObject) cls.newInstance();
        Object result = groovyObject.invokeMethod(methodName, args);
        return result;
    }

    /**
     * 加载并编译groovy文件内容，然后将类实例化，并执行对象指定的方法
     *
     * @param scriptText
     * @param args
     * @return
     */
    public Object invokeMethod(String scriptText, String alias, String methodName, Object[] args) throws IllegalAccessException, InstantiationException {
        Class<?> cls = GroovyFactory.getFactory().getGroovyClass(scriptText, alias);
        GroovyObject groovyObject = (GroovyObject) cls.newInstance();
        Object result = groovyObject.invokeMethod(methodName, args);
        return result;
    }

    /**
     * 执行groovy script 指定的方法，服务运行过程中，script被修改，会被重新编译
     * @param fileName
     * @param methodName
     * @param args
     * @return
     * @throws IOException
     * @throws ResourceException
     * @throws ScriptException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public Object invokeMethod2(String fileName,String methodName,Object args) throws ResourceException,
			ScriptException, IllegalAccessException, InstantiationException {
        //通过指定的roots来初始化GroovyScriptEngine
        GroovyObject groovyObject = (GroovyObject) gsen.loadScriptByName(fileName).newInstance();
        Object result = groovyObject.invokeMethod(methodName, args);
//        System.out.println(groovyObject.getClass().getClassLoader());
        return result;
    }

    public static void main(String[] args) throws Exception {
//        String scriptText = "def sayHello(name,age){return 'Hello,I am ' + name + ',age' + age;};" +
//                "def getTime(){return date.getTime();}";
////      String scriptText2 = "def getTime(){return new Date().getTime();}";
//        String scriptText3 = "def date = new Date();println 'Hello World! I am ' +' time:'+ name+date.getTime();";
//        String scriptText4 = "1+1";
//        Binding binding = new Binding();
//        binding.setVariable("date", new Date());
//        binding.setVariable("name", "zhangsan");
//        String alias = "script01";
//        Object resp = GroovyExecutor.getExecutor().runScript(scriptText4, binding, alias);
////        System.out.println(resp);
//
//        Map binding2 = new HashMap(1);
//        binding2.put("date", new Date());
//        resp = GroovyExecutor.getExecutor().runScriptFunction(scriptText, "getTime", binding2, null);
//        log.info(String.valueOf(resp));
//
//
//        String scriptPath = "C:\\haven.zhang\\groovy\\PaymentServiceImpl.groovy";
//        resp = GroovyExecutor.getExecutor().invokeMethod(scriptPath, "payment", null);
//        log.info(JSON.toJSONString(resp));
//
//        String scriptPath2 = "C:\\haven.zhang\\groovy\\Function.groovy";
//        resp = GroovyExecutor.getExecutor().invokeMethod(scriptPath2, "caculate", new Object[]{2, 9});
//        log.info(JSON.toJSONString(resp));
//
//
//
//        scriptText = FileUtils.readFileToString(new File(scriptPath), Charset.defaultCharset());
//        GroovyExecutor.getExecutor().invokeStaticMethod(scriptText, "test", binding2, null);
////        log.info(resp);
//
//        String[] path={"C:\\haven.zhang\\groovy\\"};
//        getExecutor().initPath(path);
//        Object reslut = getExecutor().invokeMethod2("PaymentServiceImpl.groovy", "test", new Object[]{});
//        log.info(JSON.toJSONString(reslut));
//        reslut = getExecutor().invokeMethod2("Function.groovy", "caculate", new Object[]{1, 2});
//        log.info(JSON.toJSONString(reslut));
//        reslut = getExecutor().invokeMethod2("Function.groovy", "getResp", null);
//        log.info(JSON.toJSONString(reslut));
    }
}
