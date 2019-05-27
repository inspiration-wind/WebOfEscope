package eScope;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class IpmitoolSdrList {

    public static void GetData(){
        String ip = "10.1.18.132";//服务器ip

        //服务器
        Shell shell = new Shell(ip, "root", "hdu52335335");//连接服务器
        shell.execute("ipmitool sdr list|grep -E \"PSU1_VIN|PSU1_IIN|PSU2_VIN|PSU2_IIN|Total_Power\" && date +\"%Y-%m-%d %H:%m:%S\"");//执行指令
        ArrayList<String> stdout = shell.getStandardOutput();//取得 ipmitool sdr list 和 time
        ISLdata isldate = new ISLdata(stdout);//ipmitool sdr list 和 time 转换 方便处理

        //输出要添加的数据
        System.out.println("ISL数据");
        System.out.println("ip:" + ip);
        System.out.println("power:" + isldate.power);
        System.out.println("voltage:" + isldate.voltage);
        System.out.println("ecm:" + isldate.ecm);

        System.out.println("ecm:" + isldate.ecc);
        System.out.println("server_time:" + isldate.server_time);
        System.out.println("插入数据");

        //数据库
        Connection connection = MyConnection.getConn();//数据库链接
        PreparedStatement preparedStatement = null;
        //添加数据
        try{
            String sql="INSERT INTO ipmitool_sdr_list(ip,power,voltage,ecm,ecc,server_time) VALUE('" + ip + "','" + isldate.power + "','" + isldate.voltage + "','" + isldate.ecm + "','" + isldate.ecc + "','" + isldate.server_time +"')";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            System.out.println("完成");
        } catch(Exception e){
            e.printStackTrace();
        }

    }
}

//返回数据
class ISLdata{
    int power;
    int voltage;
    int ecm;//电流测量值
    double ecc;//电流计算值
    String server_time;

    ISLdata(ArrayList<String> stdout){
        this.power = this.StI(stdout.get(4));
        this.voltage = this.StI(stdout.get(0)) + this.StI(stdout.get(2));
        this.ecm = this.StI(stdout.get(1)) + this.StI(stdout.get(3));
        BigDecimal e = new BigDecimal((double)this.power / (double)this.voltage);//计算
        this.ecc = e.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();//3位小数 四舍五入
        this.server_time = stdout.get(5);
        /*//时间String转换成Timestamp
        String timestr = new String(stdout.get(5));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try{
            date = df.parse(timestr);
        }catch (Exception e){
            e.printStackTrace();
        }
        this.time = new java.sql.Timestamp(date.getTime());
        */
    }

    //把字符串中的数据提取出来(专用)
    int StI(String str){
        String restr = new String();
        str = str.substring(19,37);
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)>=48 && str.charAt(i)<=57 &&str.charAt(i)==46){
                restr += str.charAt(i);
            }
        }
        return Integer.valueOf(restr);
    }
}

