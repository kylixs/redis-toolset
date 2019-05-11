package com.kylixs.redis.monitor;

public class ParseResult {

    int total;
    int maxQps;
    int minQps;
    double avgQps;
    int writeCmdCount;
    int readCmdCount;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getMaxQps() {
        return maxQps;
    }

    public void setMaxQps(int maxQps) {
        this.maxQps = maxQps;
    }

    public int getMinQps() {
        return minQps;
    }

    public void setMinQps(int minQps) {
        this.minQps = minQps;
    }

    public double getAvgQps() {
        return avgQps;
    }

    public void setAvgQps(double avgQps) {
        this.avgQps = avgQps;
    }

    public int getWriteCmdCount() {
        return writeCmdCount;
    }

    public void setWriteCmdCount(int writeCmdCount) {
        this.writeCmdCount = writeCmdCount;
    }

    public int getReadCmdCount() {
        return readCmdCount;
    }

    public void setReadCmdCount(int readCmdCount) {
        this.readCmdCount = readCmdCount;
    }
}