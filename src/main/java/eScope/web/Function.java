package eScope.web;

import deu.hde.webofescope.ReJson.PTTjson;
import eScope.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class Function {
    public static PTTjson WebPTT(int threshold){
        String sql = "select * from benchmark_results_summary where id in (select id from system_overview where (Hardware_Availability like '%2017' or Hardware_Availability like '%2018' or Hardware_Availability like '%2019') AND compliment='1')";
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String start_time = (df.format(new Date()));
        long currentTime = System.currentTimeMillis();
        float runtime;
        ServerUtl serverUtl = new ServerUtl(sql); //根据sql选出要模拟哪些机器
        Server[] bags = serverUtl.getservers();
        //Arrays.sort(bags);
        ArrayList<Server> results;
        if(threshold <= 1000000) {
            DCProblem kp = new DCProblem(bags, threshold, serverUtl.getserverTypeNum(), 300000); //背包算法、
            kp.solve();
            System.out.println(" -------- 该背包问题实例的解: --------- ");
            System.out.println("最优值：" + kp.getBestValue());
            System.out.println("最优解【选取的背包】: ");
            results = kp.getBestSolution();
        }else{
            Greed kp = new Greed(bags,threshold);
            kp.solution();
            System.out.println(" -------- 该背包问题实例的解: --------- ");
            System.out.println("最优值：" + kp.getBestValue());
            System.out.println("最优解【选取的背包】: ");
            results = kp.getBestSolution();
        }


        PTTjson json = new PTTjson();//Json格式

        runtime = (System.currentTimeMillis() - currentTime) / 1000f;
        double jobs = 0;
        double power = 0;
        Collections.sort(results); //按利用率从高到低排序
        System.out.println("排序后：");
        System.out.println("共有服务器：" + results.size());
        for (int i = 0; i < results.size(); i++) {
            System.out.println("id=" + results.get(i).getId() + ",Load=" + results.get(i).getutl(results.get(i).choose_utl) + ",Power=" + results.get(i).getPower(results.get(i).choose_utl) + ",value=" + results.get(i).getJobs(results.get(i).choose_utl) + ",EE=" + results.get(i).getEE(results.get(i).choose_utl) + ",chooseNum" + results.get(i).choose_utl);
            jobs = jobs + results.get(i).getJobs(results.get(i).choose_utl);
            power = power + results.get(i).getPower(results.get(i).choose_utl);
        }
        json.setJobs(jobs);
        System.out.println("总jobs数" + jobs);
        json.setPower(power);
        System.out.println("总Power数" + power);
        json.setNumber(results.size());
        System.out.println("服务器总台数" + results.size());
        serverUtl.UpdateDB(results, start_time, threshold, power, jobs, serverUtl.serverTypeNum, runtime);//更新数据库
        json.setServers(WebUpdateDB(results));
        json.setRuntime(runtime);
        System.out.println("执行耗时 : " + runtime + " 秒 ");

        return json;
    }

    public static Map<Server,Integer> WebUpdateDB(ArrayList<Server> results){
        Map<Server,Integer> map = new HashMap<Server,Integer>();
        for(Server server:results){
            if(map.containsKey(server))
                map.put(server,map.get(server) + 1);
            else
                map.put(server,1);
        }
        return map;
    }
}
