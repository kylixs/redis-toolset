package com.kylixs.redis.monitor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Redis Monitor log parser
 * @author gongdewei 4/28/19 10:17 AM
 */
public class MonitorLogParser {

    private Pattern quoteStringPattern = Pattern.compile("");

    public ParseResult parse(File file, Callback callback) throws IOException {
        return parse(new FileInputStream(file), callback);
    }

    public ParseResult parse(InputStream in, Callback callback) throws IOException{

        ParseResult result = new ParseResult();
        long seconds = -1;
        MonitorLog prevLog = null;
        List<MonitorLog> monitorLogs = new ArrayList<MonitorLog>(20480);
        Scanner scanner = new Scanner(in);
        while(scanner.hasNextLine()){
            MonitorLog log = parseLog(scanner.nextLine());
            if(log == null){
                continue;
            }
            //calc prev log duration time
            if(prevLog != null){
                prevLog.setDuration(log.getBeginTime() - prevLog.getBeginTime());
            }

            if(seconds == -1){
                seconds = log.getSeconds();
            }
            if(seconds != log.getSeconds()){
                callback.onMonitorLog(new ArrayList<MonitorLog>(monitorLogs));
                monitorLogs.clear();
                seconds = log.getSeconds();
            }
            monitorLogs.add(log);
            prevLog = log;
            result.total ++;
        }
        return result;
    }

    private MonitorLog parseLog(String line ) {
        Scanner scanner = new Scanner(line);
        MonitorLog log = new MonitorLog();
        //begin time
        String s = scanner.next();
        if("OK".equals(s)){
            return null;
        }
        int pdot = s.indexOf('.');
        long seconds = Long.parseLong(s.substring(0, pdot));
        long microsecs = Integer.parseInt(s.substring(pdot +1));
        log.setBeginTime(seconds*1000000+microsecs);
        log.setSeconds(seconds);

        //db
        s = scanner.next();
        if( !s.startsWith("[")){
            throw new RuntimeException("parse db failed: "+s);
        }
        log.setDb(Integer.parseInt(s.substring(1)));

        //client address
        s = scanner.next();
        if(!s.endsWith("]")){
            throw new RuntimeException("parse client address failed: "+s);
        }
        int p = s.indexOf(":");
        log.setClientAddress(s.substring(0,s.length()-1));
        log.setClientIp(s.substring(0, p));

        //cmd
        s = scanner.next();
        if(s.charAt(0)!='\"' && s.charAt(s.length()-1) != '\"'){
            throw new RuntimeException("parse cmd failed: "+s);
        }
        log.setCommand(s.substring(1, s.length()-1));

        //key
        try {
            s = scanner.nextLine();
            int p1 = s.indexOf("\"");
            if(p1 >= 0){
                int p2 = s.indexOf("\"", p1+1);
                if(p2 >= 0){
                    log.setKey(s.substring(p1+1, p2));
                }
            }
        } catch (NoSuchElementException e) {
        }
        return log;
    }

    public interface Callback {
        void onMonitorLog(List<MonitorLog> batch);
    }

}

//sample monitor log
//1556269354.304545 [2 172.16.152.11:60914] "SELECT" "2"
//1556269354.304552 [8 192.168.10.184:53602] "SMEMBERS" "jlcgroup:notifications:smt_quality_complain:48487D"
//1556269354.304638 [8 192.168.10.105:65055] "SCARD" "jlcgroup:notifications:pcb_quality_complain:147880W"
//1556269354.306105 [2 172.16.152.11:60914] "PING"
//1556269354.306122 [8 192.168.10.184:53602] "SCARD" "jlcgroup:notifications:worker_complain:48487D"
//1556269354.306139 [8 192.168.10.159:52575] "SCARD" "jlcgroup:notifications:steel_quality_complain:108821A"
//1556269354.306151 [8 192.168.10.105:65055] "SMEMBERS" "jlcgroup:notifications:pcb_quality_complain:147880W"
//1556269354.306791 [8 192.168.10.159:52575] "SMEMBERS" "jlcgroup:notifications:steel_quality_complain:108821A"
//1556269354.306809 [8 192.168.10.105:65055] "SCARD" "jlcgroup:notifications:steel_quality_complain:147880W"
//1556269354.307299 [10 192.168.10.105:64971] "HMSET" "spring:session:sessions:e78ed456-7020-4ca1-9e37-3b5ca997bf4d" "sessionAttr:javax.servlet.jsp.jstl.fmt.request.charset" "\xac\xed\x00\x05t\x00\x05utf-8" "lastAccessedTime" "\xac\xed\x00\x05sr\x00\x0ejava.lang.Long;\x8b\xe4\x90\xcc\x8f#\xdf\x02\x00\x01J\x00\x05valuexr\x00\x10java.lang.Number\x86\xac\x95\x1d\x0b\x94\xe0\x8b\x02\x00\x00xp\x00\x00\x01jX\xe1\xcdS"
//1556269354.307370 [2 172.16.152.11:60914] "PTTL" "cas:cross_token:CAT-b7569706-5acf-44de-9386-3fdc667775a8"
//1556269354.307693 [8 192.168.10.105:65055] "SMEMBERS" "jlcgroup:notifications:steel_quality_complain:147880W"
//1556269354.307748 [8 192.168.10.184:53602] "SMEMBERS" "jlcgroup:notifications:worker_complain:48487D"
//1556269354.308123 [2 172.16.152.11:60914] "SELECT" "2"
//1556269354.308467 [8 192.168.10.105:65055] "SCARD" "jlcgroup:notifications:smt_quality_complain:147880W"
//1556269354.308488 [10 192.168.10.105:64971] "SADD" "spring:session:expirations:1556271180000" "\xac\xed\x00\x05t\x00,expires:e78ed456-7020-4ca1-9e37-3b5ca997bf4d"
//1556269354.308514 [8 192.168.10.184:53602] "SCARD" "jlcgroup:notifications:pcb_quality_complain:36738S"
//1556269354.308779 [2 172.16.152.11:60914] "PING"
//1556269354.309246 [8 192.168.10.105:65055] "SMEMBERS" "jlcgroup:notifications:smt_quality_complain:147880W"
//1556269354.309266 [10 192.168.10.105:64971] "PEXPIRE" "spring:session:expirations:1556271180000" "2100000"
//1556269354.309477 [8 192.168.10.184:53602] "SMEMBERS" "jlcgroup:notifications:pcb_quality_complain:36738S"
//1556269354.309530 [10 192.168.10.184:53569] "HGETALL" "spring:session:sessions:c367bc3e-c06f-45b4-8eb3-9a227ccc8965"
//1556269354.309755 [2 172.16.152.11:60914] "SETEX" "cas:cat_auto_login_info_mapping:b7569706-5acf-44de-9386-3fdc667775a8" "1799" "{\"customerCode\":\"03196U\",\"autoLoginTokenUuid\":\"95b85343-7d1f-4c12-bfde-2e6a7333d196\"}"
