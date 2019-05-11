package com.kylixs.redis.monitor;

/**
 * @author gongdewei 4/28/19 10:30 AM
 */
public class MonitorLog {
    private long beginTime;
    private long seconds;
    private long duration;
    private int db;
    private String clientAddress;
    private String clientIp;
    private String command;
    private String key;
    private int dataLen;

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getDb() {
        return db;
    }

    public void setDb(int db) {
        this.db = db;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getDataLen() {
        return dataLen;
    }

    public void setDataLen(int dataLen) {
        this.dataLen = dataLen;
    }

    @Override
    public String toString() {
        return "MonitorLog{" +
                "beginTime=" + beginTime +
                ", duration=" + duration +
                ", db=" + db +
                ", clientAddress='" + clientAddress + '\'' +
                ", command='" + command + '\'' +
                ", key='" + key + '\'' +
                ", dataLen=" + dataLen +
                '}';
    }
}
