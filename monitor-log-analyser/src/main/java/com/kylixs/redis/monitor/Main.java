package com.kylixs.redis.monitor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author gongdewei 4/28/19 11:59 AM
 */
public class Main {

    public static void main(String[] args) throws Exception {
        MonitorLogParser parser = new MonitorLogParser();
//        String logfile = "/opt/projects/redis-toolset/temp/redis.log";
        String logfile = "/opt/projects/redis-toolset/temp/redis-0426-17.log";
        String outfile = logfile+".csv";
        FileOutputStream output = new FileOutputStream(outfile);
        final PrintWriter writer = new PrintWriter(new BufferedOutputStream(output));
        parser.parse(new File(logfile), new MonitorLogParser.Callback() {
            public void onMonitorLog(List<MonitorLog> batch) {
                System.out.println("parse log: "+ batch.size());
                long cost = 0, mainCost=0, mainCount=0;
                for (MonitorLog log : batch) {
                    cost += log.getDuration();
                    //log.getDuration() > 500
                    if(isFiltered(log)){
                        mainCost += log.getDuration();
                        mainCount ++;
                        if(shouldPrint(log)) {
                            System.out.println(log);
                        }
                    }
                    writer.println(toCsvString(log));
                }
                System.out.println("totalCost: "+cost+", mainCost: "+mainCost+"("+(mainCost*10000/cost/100.0)+"%), mainCount: "+mainCount);
            }
        });
        writer.close();
    }

    private static boolean shouldPrint(MonitorLog log) {
        return log.getDuration() > 100;
    }

    private static boolean isFiltered(MonitorLog log) {
//        return log.getDb() == 10 && log.getDuration() > 500;
        return log.getKey()!=null && log.getKey().startsWith("jlcgroup:notifications:");
    }

    private static String toCsvString(MonitorLog log) {
        StringBuilder sb = new StringBuilder();
        sb.append(log.getSeconds()).append(',');
        sb.append(log.getBeginTime()).append(',');
        sb.append(log.getDuration()).append(',');
        sb.append(log.getDb()).append(',');
        sb.append(log.getClientIp()).append(',');
        sb.append(log.getClientAddress()).append(',');
        sb.append(log.getCommand()).append(',');
        sb.append(log.getKey()).append(',');
        return sb.toString();
    }
}
