package com.haven.simplej.sequence;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * sequence生成器
 */
public class SequenceGenerator {
    private static final Logger logger = LoggerFactory.getLogger(SequenceGenerator.class);

    private final long epoch = 1523527707932L;

    private long sequence = 0L;

    private final long sequenceMax = 999999L;
    private String workerNo;
    private boolean needProcessId = false;

    private long lastTimestamp = -1L;

    public SequenceGenerator() {
        init();
    }

    public SequenceGenerator(boolean needProcessId) {
        this.needProcessId = needProcessId;
        init();
    }

    private void init() {
        try {
            String localIp = InetAddress.getLocalHost().getHostAddress();
            localIp = StringUtils.leftPad(localIp.split("\\.")[2] + localIp.split("\\.")[3], 6, "0");
            this.workerNo = localIp;
            if (this.needProcessId) {
                RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
                String pid = runtime.getName().split("@")[0];
                if (pid.length() > 5) {
                    pid = pid.substring(0, 5);
                }
                pid = StringUtils.leftPad(pid, 5, "0");
                this.workerNo += pid;
            }
            logger.info("初始化WorkerNo成功：" + this.workerNo);
        } catch (Exception e) {
            logger.error("初始化WorkerNoo失败", e);
            throw new RuntimeException(e);
        }
    }

    public synchronized String nextId() {
        long timestamp = getCurrentTime();
        if (this.lastTimestamp == timestamp) {
            this.sequence += 1L;
            getClass();
            if (this.sequence > 999999L) {
                timestamp = waitingNextMillis(this.lastTimestamp);
                this.sequence = 0L;
            }
        } else {
            this.sequence = 0L;
        }

        if (timestamp < this.lastTimestamp) {
            logger.error("clock moved backwards.Refusing to generate id for {} milliseconds",(this.lastTimestamp - timestamp));
            throw new IllegalStateException(String.format("clock moved backwards.Refusing to generate id for %d milliseconds", (this.lastTimestamp - timestamp)));
        }

        this.lastTimestamp = timestamp;

        getClass();
        StringBuffer result = new StringBuffer().append(timestamp - 1523527707932L)
                .append(this.workerNo)
                .append(StringUtils.leftPad(this.sequence + "", 6, "0"));
        return result.toString();
    }

    private long waitingNextMillis(long lastTimestamp) {
        long timestamp = getCurrentTime();
        while (timestamp <= lastTimestamp) {
            timestamp = getCurrentTime();
        }
        return timestamp;
    }

    private long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public boolean isNeedProcessId() {
        return this.needProcessId;
    }

    public void setNeedProcessId(boolean needProcessId) {
        this.needProcessId = needProcessId;
    }

    public static void main(String[] args) throws Exception {
        SequenceGenerator sequenceGenerator = new SequenceGenerator(false);
        System.out.println(sequenceGenerator.workerNo);

        CountDownLatch downLatch = new CountDownLatch(1000000);
        long start = System.currentTimeMillis();
        Map map = new HashMap();
        for (int i = 0; i < 1000000; i++) {
//            sequenceGenerator.nextId();
//            System.out.println(id.length() + " " + id);
//            CommonUtils.createNewId();
            new Thread(sequenceGenerator.new Worker(sequenceGenerator,downLatch)).start();
        }
        downLatch.await();
        long time = System.currentTimeMillis() - start;
        System.out.println("耗时=" + time + " 总数=" + map.entrySet().size());
    }

    class Worker implements Runnable{
        SequenceGenerator sequenceGenerator;
        CountDownLatch downLatch;
        Worker(SequenceGenerator sequenceGenerat,CountDownLatch downLatch){
            this.sequenceGenerator = sequenceGenerat;
            this.downLatch = downLatch;
        }
        @Override
        public void run() {
//            sequenceGenerator.nextId();
            SequenceUtil.generateId();
            downLatch.countDown();
        }
    }
}
