package com.haven.simplej.script.groovy;

import groovy.lang.Script;
import lombok.Getter;
import lombok.Setter;

/**
 * @author haven.zhang on 2018/9/23.
 */
@Setter
@Getter
public class GroovyFile {
    private long expireTime;
    private Class<?> cls;
    private String md5;
    private Script script;
    private long lastModify;

    public GroovyFile(Class<?> cls, String md5) {
        this.cls = cls;
        this.md5 = md5;
    }

    public GroovyFile(Script script, String md5) {
        this.script = script;
        this.md5 = md5;
    }

    public GroovyFile(Class<?> cls, long lastModify) {
        this.cls = cls;
        this.lastModify = lastModify;
    }

    public GroovyFile(Script script, long lastModify) {
        this.script = script;
        this.lastModify = lastModify;
    }

}