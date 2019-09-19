package com.leyou.es;


import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;

import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.*;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;


public class ElasticSearchManager {

    TransportClient client = null;

    @Before
    public void initClient() throws Exception {
        client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
    }


    @Test
    public void testQuery() {
        //1.构建term查询
//         QueryBuilder qb= QueryBuilders.termQuery("goodsName", "小米");
        //2.通配符查询
//         QueryBuilder qb= QueryBuilders.wildcardQuery("goodsName", "*华*");
        //3.模糊查询（容错）
//         FuzzyQueryBuilder qb= QueryBuilders.fuzzyQuery("goodsName", "化为");
//         qb.fuzziness(Fuzziness.ONE);
        //4.区间查询
//        QueryBuilder qb = QueryBuilders.rangeQuery("price").gte(2000).lte(4000);
        //5.分词查询
//         QueryBuilder qb= QueryBuilders.matchQuery("goodsName", "小米9手机");
        //6.查询所有
//         QueryBuilder qb= QueryBuilders.matchAllQuery();
        //7.组合查询
        QueryBuilder qb = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("goodsName", "手机"))
                .mustNot( QueryBuilders.termQuery("goodsName","小米"));  //组合查询


        SearchResponse searchResponse = client.prepareSearch("heima")//执行查询的索引库
                .setQuery(qb) //把构建的查询 放入请求中生效
                .get();  //执行
        long totalHits1 = searchResponse.getHits().getTotalHits();
        System.out.println("总数为："+totalHits1);
        SearchHit[] totalHits = searchResponse.getHits().getHits();
        for (SearchHit totalHit : totalHits) {
            String sourceAsString = totalHit.getSourceAsString();
            System.out.println(sourceAsString);
        }

    }


    @After
    public void end() {
        client.close();
    }
}
