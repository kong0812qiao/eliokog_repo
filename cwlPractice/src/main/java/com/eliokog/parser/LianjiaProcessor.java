package com.eliokog.parser;

import com.eliokog.core.CrawlerContext;
import com.eliokog.fetcher.FetcherResult;
import com.eliokog.url.WebURL;
import com.eliokog.util.URLUtils;
import com.eliokog.util.Validator;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by eliokog on 2017/2/8.
 * processor to crawler Lianjia
 */
public class LianjiaProcessor implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(LianjiaProcessor.class);

    @Override
    public FetcherResult process(FetcherResult result) {
        Document doc = Jsoup.parse(result.getContent());
        Elements liSet = doc.select("div.info-panel");

        LinkedHashSet<WebURL> parsedLinkSet = new LinkedHashSet<WebURL>();
        LinkedHashMap<String, String> parsedValMap = new LinkedHashMap<>();
        //TODO add duplicate removal logic here

/*        for(Element e : liSet){
            StringBuilder sb = new StringBuilder();
            Elements nameLocationSet = e.select("a[href]");
//            Elements price = e.select("div.div-cun");
            Elements price = e.select("div.fl");
            price.add( e.select("div.fr").get(1));
            //wrap HTML info
            getCommunityHTMLInfo(nameLocationSet);
            getPriceHTMLInfo( price);
            nameLocationSet.forEach(s ->{
                String fullURL = URLUtils.getFullURL(s.attr("href"),result);
                logger.debug("full url: {}", fullURL);
                //complete the full URL
                s.attr("href", fullURL);
                sb.append(s).append("\r\n");
            });
            sb.append("\r\n").append("<br> 价格 </br> ").append("\r\n");
            price.forEach(s ->{
                sb.append(s).append("\r\n");
            });
            result.setParsedList(new LinkedList<WebURL>(parsedLinkSet));
            logger.debug("Data: {}", sb);
            parsedValMap.put("<br>房源信息: </br>", sb.toString());
        }*/
        int i = 1;
        for(Element e : liSet){
            String info = getExcelString(e, result.getUrl());
            result.setParsedList(new LinkedList<WebURL>(parsedLinkSet));
            logger.debug("Data: {}", info);
            parsedValMap.put(String.valueOf(i), info);
            CrawlerContext.context().getPersitEnQService().enQueue(info);
            i++;
        }

        logger.debug("parsedValMap size: {}, parsedValMap: {}" ,parsedValMap.size(), parsedValMap);

        result.setFieldMap(parsedValMap);
        return result;
    }

    private Elements getCommunityHTMLInfo( Elements nameLocationSet){
        StringBuilder sb = new StringBuilder();
        String[] locate = nameLocationSet.get(0).text().split(" ");
        sb.append("小区： ").append(locate[0]).append(", 户型： ").append(locate[1]).append(" ，面积： "+ locate[2]);
        nameLocationSet.get(0).text( sb.toString());
        return nameLocationSet;
    }

    private Elements getPriceHTMLInfo( Elements price){
        price.get(0).text(("成交日期："+price.get(0).text()));
        price.get(1).text(("单价："+price.get(1).text()));
        price.get(0).text(("总价："+price.get(2).text()));
        return price;
    }

    public String getExcelString(Element data, WebURL url){
        Elements e = data.select("a[href]");
        String link = URLUtils.getFullURL(e.get(0).attr("href"),url);
        String[] val = StringUtils.split(e.get(0).text(), " ");
        String name = "", type="", sqr="";
        if(val.length==3){
           name = val[0];
           type = val[1];
           sqr = val[2];
        }
        String number = data.select("a[key]").attr("key");
        Elements info = data.select("div.col-1");//<div class="col-1 fl">长宁 中山公园 | 高区/24层 | 朝南 | 精装 满五 距离2号线江苏路站737米
        String[] infos = StringUtils.split(info.text(), "|");
        String disctict = StringUtils.split(infos[0], " ")[0];
        String area =  StringUtils.split(infos[0], " ")[1];
        String floor = infos[1];
        String orientation = infos[2];

        String decoration = "N/A";
        if(infos.length>3) {
            decoration = StringUtils.split(infos[3], " ")[0];
        }
        String state = "N/A", line = "N/A", station = "N/A", introduction ="N/A";
        Elements otherInfo = data.select("div.introduce");
        if(null!=otherInfo && otherInfo.size()>0){
            String[] introduce =  StringUtils.split(otherInfo.text()," ");
            if(introduce.length>0){
             for(String s : introduce){
                    if(s.startsWith("距")){
                        introduction = s;
                        line = StringUtils.mid(s, 2, s.indexOf("线")-1);
                        station = StringUtils.mid(s, s.indexOf("线")+1, findFirstNumber(s)-1);

                    }
                    if(s.startsWith("满")){
                        state =s;
                    }

                }
            }
        }

        Elements price = data.select("div.fr");//<div class="col-2 fr"> 2016-11-10 链家网签约 67092 元/平 挂牌单价 1050 万 挂牌总价

        String[] priceInfo = StringUtils.split(price.get(0).text(), " ");
        String signDate = priceInfo[0];
        String priceSqr = priceInfo [2];
        String totalPrice = priceInfo [4];
        StringBuilder sb = new StringBuilder();
        sb.append(number).append("%").append(name).append("%").append(priceSqr).append("%")
                .append(totalPrice).append("%").append(type).append("%").append(floor).append("%")
                .append(disctict).append("%").append(area).append("%").append(signDate).append("%")
                .append(sqr).append("%").append(state).append("%").append(line).append("%")
                .append(station).append("%").append(orientation).append("%").append(decoration)
                .append("%").append(introduction).append("%").append(link);
        logger.debug(sb.toString());
        return sb.toString();
    }

    public int findFirstNumber (String s){
        s = StringUtils.substring(s, s.indexOf("线"));
        Pattern pattern = Pattern.compile("^\\D*(\\d)");
        Matcher matcher = pattern.matcher(s);
        matcher.find();
        return matcher.start(1);
    }

}
