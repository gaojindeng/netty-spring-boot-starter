package io.github.gaojindeng.netty.properties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gjd
 */
public class AbstractProperties {
    private Integer port;
    private List<LoadClass> sharableHandlers = new ArrayList<>();
    private List<LoadClass> noSharableHandlers = new ArrayList<>();
    private int readerIdleSeconds = 0;
    private int writerIdleSeconds = 0;
    private int allIdleSeconds = 0;
    private int maxConn = 1000;
    /**
     * 对应netty的worker线程数
     */
    private int ioThreads = 0;

    public static class LoadClass {
        private String className;
        private List<ParamClass> params;

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public List<ParamClass> getParams() {
            return params;
        }

        public void setParams(List<ParamClass> params) {
            this.params = params;
        }
    }

    public static class ParamClass {
        private String className;
        private Object value;

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public List<LoadClass> getSharableHandlers() {
        return sharableHandlers;
    }

    public void setSharableHandlers(List<LoadClass> sharableHandlers) {
        this.sharableHandlers = sharableHandlers;
    }

    public List<LoadClass> getNoSharableHandlers() {
        return noSharableHandlers;
    }

    public void setNoSharableHandlers(List<LoadClass> noSharableHandlers) {
        this.noSharableHandlers = noSharableHandlers;
    }

    public int getReaderIdleSeconds() {
        return readerIdleSeconds;
    }

    public void setReaderIdleSeconds(int readerIdleSeconds) {
        this.readerIdleSeconds = readerIdleSeconds;
    }

    public int getWriterIdleSeconds() {
        return writerIdleSeconds;
    }

    public void setWriterIdleSeconds(int writerIdleSeconds) {
        this.writerIdleSeconds = writerIdleSeconds;
    }

    public int getAllIdleSeconds() {
        return allIdleSeconds;
    }

    public void setAllIdleSeconds(int allIdleSeconds) {
        this.allIdleSeconds = allIdleSeconds;
    }

    public int getMaxConn() {
        return maxConn;
    }

    public void setMaxConn(int maxConn) {
        this.maxConn = maxConn;
    }

    public int getIoThreads() {
        return ioThreads;
    }

    public void setIoThreads(int ioThreads) {
        this.ioThreads = ioThreads;
    }
}
