package com.allen.spider.job;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.allen.spider.common.HttpMethod;
import com.allen.spider.utils.HttpSpiderUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author xuguocai  @date 2022/4/18 09:48
 */
@Slf4j
public class QichemenServiceJob {

    /**
     * 搜狐国内新车地址
     */
    private static  String soHuUrl = "http://v2.sohu.com/author-page-api/author-articles/pc/430526?columnId=161899&secretStr=71df96caffeada942f7bb9babe521400&pNo=";

    private static String viewUrl ="http://v2.sohu.com/author-page-api/articles/pv?&callback=?&articleIds=";

    private static String qichemenUrl ="https://www.qichemen.com/xw_3.html";
    protected Map<String, String> headerMap = new HashMap<String, String>() {{
        put("Connection", "keep-alive");
        put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        put("Accept-Encoding", "gzip, deflate, sdch");
        put("Accept-Language", "zh-CN,zh;q=0.9");
        put("Redis-Control", "max-age=0");
        put("Upgrade-Insecure-Requests", "1");
        put("typeFlg","9");//此处增加浏览器端访问IP
    }};

    public void getSohuNewData() {
        // 20页数据为基础
        for (int i=1;i<20;i++){
            String tmpUrl = soHuUrl+i;
            String result = HttpUtil.get(tmpUrl);

            if (StrUtil.isBlank(result)){
                return;
            }

            JSONObject jsonObject = JSON.parseObject(result);
            Integer code = jsonObject.getInteger("code");
            if (200 != code){
                log.debug("响应版本异常：{}",jsonObject.toJSONString());
                break;
            }

            soHuExtracted(jsonObject);

            // 5s执行
            try{
                TimeUnit.SECONDS.sleep(5);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private void soHuExtracted(JSONObject jsonObject) {
        JSONObject data = jsonObject.getJSONObject("data");
        String articleVOS = data.getString("pcArticleVOS");
        log.info(articleVOS);
    }

    public void getQichemenData() {

        String content = null;
        for (int i=0;i<5;i++){
            if (0 == i){
                content = HttpSpiderUtil.send(qichemenUrl,null,headerMap,null,null,null, HttpMethod.GET);
            }else {
                // 5s执行
                try{
                    TimeUnit.SECONDS.sleep(5);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Map<String, String> formParamMap = new HashMap<>(10);
                formParamMap.put("pstart",String.valueOf(i));
                formParamMap.put("categoryid","3");
                content = HttpSpiderUtil.send(qichemenUrl,null,headerMap,formParamMap,null,null, HttpMethod.POST);
            }

            if (StrUtil.isBlank(content) || "Error".equals(content)){
                log.error("爬取汽车门数据异常,执行次数:{},异常：{}",i,content);
                break;
            }

            qichemmenExtracted(content);
        }

    }

    private void qichemmenExtracted(String content) {
        //通过Jsoup进行页面解析
        Document document = Jsoup.parse(content);

        //根据网页标签解析源码
        Elements elements = document.select(".item");

        Date date = new Date();
        for(Element element:elements){

            Elements h4Ele = element.select("h4");
            String aname = h4Ele.select("a").text();
            String aUrl =h4Ele.select("a").attr("href");

            //将解析后的实体放入集合中
            String text = HttpSpiderUtil.send(aUrl,null,headerMap,null,null,null, HttpMethod.GET);
            Document doc = Jsoup.parse(text);
            //根据网页标签解析源码
            Elements mains = doc.select(".b-group-subtit");
            Element msgItem = mains.get(0);
            Elements spans = msgItem.select("span");

            String newsDate = spans.get(0).text();
            String text4 = spans.get(3).text().substring(4);
            Integer views = Integer.valueOf(text4);

            System.out.println("newsDate:"+newsDate+" views:"+views);
        }
    }

    public static void main(String[] args) {

//        String url = "https://www.qichemen.com/xw25917.html";
//        String tmp = url.replace("https://www.qichemen.com/xw","");
//        System.out.println(tmp);
//
//        String tmp2 = tmp.replace(".html","");
//
//        System.out.println(tmp2);

        QichemenServiceJob qichemenServiceJob = new QichemenServiceJob();
        qichemenServiceJob.getQichemenData();

    }
}
