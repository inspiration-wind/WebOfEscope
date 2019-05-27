package deu.hde.webofescope.ReJson;

import eScope.Server;

import java.util.ArrayList;
import java.util.Map;

public class PTTjson {
    private double jobs;//总jobs数
    private double power;//总Power数
    private Integer number;//服务器总台数
    //private Integer mapSize;
    private ArrayList<PTTserver> servers = new ArrayList<>();//整合的选中的服务器信息
    private float runtime;

    public void setJobs(double jobs){this.jobs = jobs;}
    public void setPower(double power){this.power = power;}
    public void setNumber(Integer number){this.number = number;}
    //public void setMapSize(Integer mapSize){this.mapSize = mapSize;}
    public void setRuntime(float runtime){this.runtime = runtime;}
    public void setServers(Map<Server,Integer> map){
        for(Map.Entry<Server,Integer> entry:map.entrySet()){ //result表
            int choose_utl = entry.getKey().getChoose_utl();
            servers.add(new PTTserver(entry.getKey().getId(),entry.getKey().getutl(choose_utl),entry.getKey().getPower(choose_utl),entry.getKey().getJobs(choose_utl),entry.getKey().getEE(choose_utl),entry.getValue()));
        }
    }

    public double getJobs(){return this.jobs;}
    public double getPower(){return this.power;}
    public Integer getNumber(){return this.number;}
    //public Integer getMapSize(){return this.mapSize;}
    public ArrayList<PTTserver> getServers(){return this.servers;}
    public float getRuntime(){return this.runtime;}

}

class PTTserver{
    public String id;
    public String utl;
    public double power;
    public int jobs;
    public double EE;
    public int num;

    PTTserver(String id,String utl,double power,int jobs,double EE,int num){
        this.id = id;
        this.utl = utl;
        this.power = power;
        this.jobs = jobs;
        this.EE = EE;
        this.num = num;
    }

//    public void setId(String id) { this.id = id; }
//    public void setUtl(String utl) { this.utl = utl; }
//    public void setPower(double power) { this.power = power; }
//    public void setJobs(int jobs) { this.jobs = jobs; }
//    public void setEE(double EE) { this.EE = EE; }
//    public void setNum(int num) { this.num = num; }
//
//    public String getId() { return id; }
//    public String getUtl() { return utl; }
//    public double getPower() { return power; }
//    public int getJobs() { return jobs; }
//    public double getEE() { return EE; }
//    public int getNum() { return num; }

}