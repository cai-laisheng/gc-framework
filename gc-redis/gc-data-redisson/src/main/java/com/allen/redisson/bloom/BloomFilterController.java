package com.allen.redisson.bloom;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuguocai on 2021/8/2 15:47  调用
 */

@RequestMapping("/bloom")
@RestController
public class BloomFilterController {

    private BloomFilterUtil bloomFilterUtil;


    /**
     * 初始化数据
     */
    @RequestMapping("/initBloomFilter")
    public void initBloomFilter() {
        List<String> list = new ArrayList<>();

        for (int i =0; i<200;i++){
            list.add(String.valueOf(i));
        }

        bloomFilterUtil.initBloomFilter("bloom",list);

    }

    /**
     *  向布隆漏斗添加一个值
     *
     * @param orderKey
     * @return
     */
    @RequestMapping("/addBloom")
    public String addBloom(@RequestParam("orderNum") String orderKey) {

        try {
            bloomFilterUtil.put("bloom",orderKey);
        }catch (Exception e){
            e.printStackTrace();
        }

        return  "success";
    }


    /**
     * 检查 Key 是否存在
     * @param orderKey
     * @return
     */
    @RequestMapping("/check")
    public boolean checkBloomFilter(@RequestParam("orderNum") String orderKey) {

        boolean blooom = false;

        try {
            blooom = bloomFilterUtil.mightContain("blooom", orderKey);
        }catch (Exception e){
            e.printStackTrace();
        }
        return blooom;

    }


}
