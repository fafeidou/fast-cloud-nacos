package fast.cloud.nacos.rest.client.example;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.term.TermSuggestion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class FastCommonEsRestClientExampleApplicationTests {
    @Autowired
    private RestHighLevelClient client;

    @Test
    public void testCreateIndex() throws IOException {
        //创建索引请求对象
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("hello_es");
        createIndexRequest.settings(Settings.builder().put("number_of_shards", 2)
                .put("number_of_replicas", 1).build());
        createIndexRequest.mapping("doc", "{\n" +
                "\t\"properties\": {\n" +
                "\t\t\"name\": {\n" +
                "\t\t\t\"type\": \"text\",\n" +
                "\t\t\t\"analyzer\":\"ik_max_word\",\n" +
                "           \"search_analyzer\":\"ik_smart\"\n" +
                "\t\t},\n" +
                "\t\t\"description\": {\n" +
                "\t\t\t\"type\": \"text\",\n" +
                "\t\t\t\"analyzer\": \"ik_max_word\",\n" +
                "            \"search_analyzer\":\"ik_smart\"\n" +
                "\t\t\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}", XContentType.JSON);
        //创建索引客户端
        IndicesClient indices = client.indices();
        CreateIndexResponse createIndexResponse = indices.create(createIndexRequest, RequestOptions.DEFAULT);
        boolean acknowledged = createIndexResponse.isAcknowledged();
        log.info("isAcknowledged:{}", acknowledged);
    }

    @Test
    public void testDeleteIndex() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("hello_es");
        AcknowledgedResponse deleteIndexResponse = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        boolean acknowledged = deleteIndexResponse.isAcknowledged();
        log.info("isAcknowledged:{}", acknowledged);

    }

    @Test
    public void testAddDoc() throws IOException {
        IndexRequest indexRequest = new IndexRequest("hello_es", "doc");
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", "springcloud");
        jsonMap.put("description", "本课程主要从四个章节进行讲解: 1.微服务架构入门 2.spring cloud基础入门3.实战SpringBoot4.注册中心eureka");
        indexRequest.source(jsonMap);
        client.index(indexRequest, RequestOptions.DEFAULT);
    }

    @Test
    public void getDoc() throws IOException {
        GetRequest getRequest = new GetRequest(
                "hello_es",
                "doc",
                "gdbSpW4BXv1xyLWLmtrS");

        //选择返回的字段
        String[] includes = new String[]{"name", "description", "studymodel"};
        String[] excludes = Strings.EMPTY_ARRAY;
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
        getRequest.fetchSourceContext(fetchSourceContext);

        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);

        Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
        System.out.println(sourceAsMap);
    }

    //更新文档
    @Test
    public void updateDoc() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("hello_es", "doc",
                "dda3pW4BXv1xyLWLjNpZ");
        Map<String, String> map = new HashMap<>();
        map.put("name", "Spring Cloud实战");
        updateRequest.doc(map);
        UpdateResponse update = client.update(updateRequest, RequestOptions.DEFAULT);
        RestStatus status = update.status();
        System.out.println(status);
    }

    //批量插入
    @Test
    public void testBulkAdd() throws IOException {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", "spring cloud实战");
        jsonMap.put("description", "本课程主要从四个章节进行讲解: 1.微服务架构入门 2.spring cloud基础入门3.实战SpringBoot4.注册中心eureka");
        jsonMap.put("studymodel", "201001");

        BulkRequest request = new BulkRequest();
        request.add(new IndexRequest("hello_es", "doc")
                .source(jsonMap));
        request.add(new IndexRequest("hello_es", "doc")
                .source(jsonMap));
        request.add(new IndexRequest("hello_es", "doc")
                .source(jsonMap));
        BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
        Stream.of(bulkResponse.getItems()).forEach(model -> log.info("index:{},response:{}",
                model.getIndex(), model.getResponse()));
    }

    //查询搜索记录
    @Test
    public void testSearchAll() throws IOException {
        SearchRequest searchRequest = new SearchRequest("hello_es");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        //设置需要显示的字段
        searchSourceBuilder.fetchSource(new String[]{"name", "description", "studymodel"}, Strings.EMPTY_ARRAY);
        //分页查询，设置起始下标，从0开始
        searchSourceBuilder.from(0);
        //每页显示个数
        searchSourceBuilder.size(2);

        //指定排序
        searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
//        searchSourceBuilder.sort(new FieldSortBuilder("name").order(SortOrder.ASC));

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        Stream.of(searchHits).forEach(model -> {
            String sourceAsString = model.getSourceAsString();
            log.info("sourceAsString:{}", sourceAsString);
        });
    }

    //Term Query
    @Test
    public void testTermQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("hello_es");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        //查询包括spring的结果
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("name", "spring");
        searchSourceBuilder.query(termQueryBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        Stream.of(searchHits).forEach(model -> {
            String sourceAsString = model.getSourceAsString();
            log.info("sourceAsString:{}", sourceAsString);
        });
    }

    //根据id精确匹配
    @Test
    public void testQueryId() throws IOException {
        SearchRequest searchRequest = new SearchRequest("hello_es");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        String[] ids = new String[]{"gdbSpW4BXv1xyLWLmtrS"};
        List<String> idList = Arrays.asList(ids);
        searchSourceBuilder.query(QueryBuilders.termsQuery("_id", idList));
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        Stream.of(searchHits).forEach(model -> {
            String sourceAsString = model.getSourceAsString();
            log.info("sourceAsString:{}", sourceAsString);
        });
    }

    //matchQuery
    @Test
    public void testMatchQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("hello_es");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.matchQuery("name", "spring开发").operator(Operator.OR));
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        Stream.of(searchHits).forEach(model -> {
            String sourceAsString = model.getSourceAsString();
            log.info("sourceAsString:{}", sourceAsString);
        });
    }

    //minimum_should_match
    @Test
    public void testMinimumShouldMatch() throws IOException {
        SearchRequest searchRequest = new SearchRequest("hello_es");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.matchQuery("description", "基础入门你好").minimumShouldMatch("80%"));
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        Stream.of(searchHits).forEach(model -> {
            String sourceAsString = model.getSourceAsString();
            log.info("sourceAsString:{}", sourceAsString);
        });
    }

    //multi Query and boost
    @Test
    public void testMultiQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("hello_es");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder multiMatchQueryBuilder =
                QueryBuilders.multiMatchQuery("实战", "description", "name");
        multiMatchQueryBuilder.field("name", 10);
        searchSourceBuilder.query(multiMatchQueryBuilder);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        Stream.of(searchHits).forEach(model -> {
            String sourceAsString = model.getSourceAsString();
            log.info("sourceAsString:{}", sourceAsString);
        });
    }

    //bool query
    @Test
    public void testBoolQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("hello_es");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder multiMatchQueryBuilder =
                QueryBuilders.multiMatchQuery("实战", "description", "name");
        multiMatchQueryBuilder.field("name", 10);
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("studymodel", 201001);
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(termQueryBuilder).must(multiMatchQueryBuilder);
        searchSourceBuilder.query(boolQueryBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        Stream.of(searchHits).forEach(model -> {
            String sourceAsString = model.getSourceAsString();
            log.info("sourceAsString:{}", sourceAsString);
        });
    }

    //filter
    @Test
    public void testFilter() throws IOException {
        SearchRequest searchRequest = new SearchRequest("hello_es");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder multiMatchQueryBuilder =
                QueryBuilders.multiMatchQuery("实战", "description", "name");
        multiMatchQueryBuilder.field("name", 10);
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("studymodel", 201001);
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(termQueryBuilder).must(multiMatchQueryBuilder);
        searchSourceBuilder.query(boolQueryBuilder);


        boolQueryBuilder.filter(QueryBuilders.termQuery("studymodel", 201001));
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        Stream.of(searchHits).forEach(model -> {
            String sourceAsString = model.getSourceAsString();
            log.info("sourceAsString:{}", sourceAsString);
        });
    }

    //查询搜索记录
    @Test
    public void testHeightLight() throws IOException {
        SearchRequest searchRequest = new SearchRequest("hello_es");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("name", "曾经"));

        searchSourceBuilder.fetchSource(new String[]{"name", "description"}, Strings.EMPTY_ARRAY);
        // 设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.requireFieldMatch(true).field("name")
                .preTags("<strong>").postTags("</strong>");
        searchSourceBuilder.highlighter(highlightBuilder);


        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        Stream.of(searchHits).forEach(model -> {
            String sourceAsString = model.getSourceAsString();
            Map<String, HighlightField> highlightFields = model.getHighlightFields();
            String name = (String) model.getSourceAsMap().get("name");
            if (highlightFields != null) {
                HighlightField nameField = highlightFields.get("name");
                if (nameField != null) {
                    Text[] fragments = nameField.getFragments();
                    StringBuffer stringBuffer = new StringBuffer();
                    for (Text str : fragments) {
                        stringBuffer.append(str.string());
                    }
                    name = stringBuffer.toString();
                }
            }

            log.info("name:{}=====sourceAsString:{}", name, sourceAsString);
        });
    }

    @Test
    public void testSuggest() throws IOException {
        SearchRequest searchRequest = new SearchRequest("hello_es");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        //做查询建议
        //词项建议
        SuggestionBuilder termSuggestionBuilder =
                SuggestBuilders.termSuggestion("name").text("曾经沧海难");
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("suggest_user", termSuggestionBuilder);
        searchSourceBuilder.suggest(suggestBuilder);

        searchRequest.source(searchSourceBuilder);
        //3、发送请求
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        //4、处理响应
        //搜索结果状态信息
        if (RestStatus.OK.equals(searchResponse.status())) {
            // 获取建议结果
            Suggest suggest = searchResponse.getSuggest();
            TermSuggestion termSuggestion = suggest.getSuggestion("suggest_user");
            for (TermSuggestion.Entry entry : termSuggestion.getEntries()) {
                log.info("text: " + entry.getText().string());
                for (TermSuggestion.Entry.Option option : entry) {
                    String suggestText = option.getText().string();
                    log.info("   suggest option : " + suggestText);
                }

            }
        }
    }

    //Highlight
    @Test
    public void testHighlight() throws IOException {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("hello_es");
        //指定类型
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置源字段过虑,第一个参数结果集包括哪些字段，第二个参数表示结果集不包括哪些字段
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel", "price", "timestamp"}, new String[]{});

        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<tag>");
        highlightBuilder.postTags("</tag>");
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
//        highlightBuilder.fields().add(new HighlightBuilder.Field("description"));
        searchSourceBuilder.highlighter(highlightBuilder);

        //向搜索请求对象中设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索,向ES发起http请求
        SearchResponse searchResponse = client.search(searchRequest);
        //搜索结果
        SearchHits hits = searchResponse.getHits();
        //匹配到的总记录数
        long totalHits = hits.getTotalHits();
        //得到匹配度高的文档
        SearchHit[] searchHits = hits.getHits();
        //日期格式化对象
        for (SearchHit hit : searchHits) {
            //文档的主键
            String id = hit.getId();
            //源文档内容
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            //源文档的name字段内容
            String name = (String) sourceAsMap.get("name");
            //取出高亮字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields != null) {
                //取出name高亮字段
                HighlightField nameHighlightField = highlightFields.get("name");
                if (nameHighlightField != null) {
                    Text[] fragments = nameHighlightField.getFragments();
                    StringBuffer stringBuffer = new StringBuffer();
                    for (Text text : fragments) {
                        stringBuffer.append(text);
                    }
                    name = stringBuffer.toString();
                }
            }

            //由于前边设置了源文档字段过虑，这时description是取不到的
            String description = (String) sourceAsMap.get("description");
            //学习模式
            String studymodel = (String) sourceAsMap.get("studymodel");
            //价格
            Double price = (Double) sourceAsMap.get("price");
            //日期
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }

    }

    @Test
    public void testAgg() {

        try {
            Map<String, Long> groupMap = getTermsAgg(QueryBuilders.matchAllQuery(), "name", "hello_es");
            groupMap.forEach((key, value) -> System.out.println(key + " -> " + value.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Long> getTermsAgg(QueryBuilder queryBuilder, String field, String... indexs) throws IOException {
        Map<String, Long> groupMap = new HashMap<>();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(0);

        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("agg").field(field);
        searchSourceBuilder.aggregation(aggregationBuilder);

        SearchRequest searchRequest = new SearchRequest(indexs);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Terms terms = searchResponse.getAggregations().get("agg");
        for (Terms.Bucket entry : terms.getBuckets()) {
            groupMap.put(entry.getKey().toString(), entry.getDocCount());
        }
        return groupMap;
    }


    public Map<String, Map<String, Long>> getTermsAggTwoLevel(QueryBuilder queryBuilder, String field1, String field2, String... indexs) throws IOException {
        Map<String, Map<String, Long>> groupMap = new HashMap<>();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(0);

        AggregationBuilder agg1 = AggregationBuilders.terms("agg1").field(field1);
        AggregationBuilder agg2 = AggregationBuilders.terms("agg2").field(field2);
        agg1.subAggregation(agg2);
        searchSourceBuilder.aggregation(agg1);

        SearchRequest searchRequest = new SearchRequest(indexs);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Terms terms1 = searchResponse.getAggregations().get("agg1");
        Terms terms2;
        for (Terms.Bucket bucket1 : terms1.getBuckets()) {
            terms2 = bucket1.getAggregations().get("agg2");
            Map<String, Long> map2 = new HashMap<>();
            for (Terms.Bucket bucket2 : terms2.getBuckets()) {
                map2.put(bucket2.getKey().toString(), bucket2.getDocCount());
            }
            groupMap.put(bucket1.getKey().toString(), map2);
        }
        return groupMap;
    }


}
