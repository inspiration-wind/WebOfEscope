package deu.hde.webofescope;

import deu.hde.webofescope.ReJson.*;
import eScope.*;
import eScope.web.Function;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/main")
public class Controller {

    @RequestMapping(value = "/ipmitool_sdr_list", method = RequestMethod.GET)
    public String ipmitoolsdrlist(){
        IpmitoolSdrList.GetData();
        return "ipmitool sdr list 写入数据库";
    }

    @RequestMapping(value = "/data/{power}",method = RequestMethod.GET)
    public PTTjson PTT(@PathVariable Integer power){
        return Function.WebPTT(power);
    }

}
