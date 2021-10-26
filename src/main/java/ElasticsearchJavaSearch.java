import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ElasticsearchJavaSearch {

    private static String INDEX_NAME = "twitter";
    private static RestHighLevelClient client = null;

    private static synchronized RestHighLevelClient makeConnection() {
        final BasicCredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();
        basicCredentialsProvider
                .setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", "password"));

        if (client == null) {
            client = new RestHighLevelClient(
                    RestClient.builder(new HttpHost("localhost", 9200, "http"))
                            .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                                @Override
                                public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                                    httpClientBuilder.disableAuthCaching();
                                    return httpClientBuilder.setDefaultCredentialsProvider(basicCredentialsProvider);
                                }
                            })
            );
        }

        return client;
    }

    public static void main(String[] args) throws IOException {
        client = makeConnection();

        // Search 1: Search for all documents
        System.out.println("****************** Search 1");
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        Map<String, Object> map=null;

        try {
            SearchResponse searchResponse = null;
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            if (searchResponse.getHits().getTotalHits().value > 0) {
                SearchHit[] searchHit = searchResponse.getHits().getHits();
                for (SearchHit hit : searchHit) {
                    map = hit.getSourceAsMap();
                    System.out.println("map:" + Arrays.toString(map.entrySet().toArray()));

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Search 2:
        System.out.println("****************** Search 2");
        SearchSourceBuilder builder = new SearchSourceBuilder()
                .postFilter(QueryBuilders.rangeQuery("age").from(25).to(30));

        SearchRequest searchRequest2 = new SearchRequest();
        searchRequest2.indices(INDEX_NAME);
        searchRequest2.searchType(SearchType.DFS_QUERY_THEN_FETCH);
        searchRequest2.source(builder);

        try {
            SearchResponse searchResponse = null;
            searchResponse = client.search(searchRequest2, RequestOptions.DEFAULT);
            if (searchResponse.getHits().getTotalHits().value > 0) {
                SearchHit[] searchHit = searchResponse.getHits().getHits();
                for (SearchHit hit : searchHit) {
                    map = hit.getSourceAsMap();
                    System.out.println("map:" + Arrays.toString(map.entrySet().toArray()));

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Search 3:
        System.out.println("****************** Search 3");
        SearchSourceBuilder builder3 = new SearchSourceBuilder();
        builder3.from(0);
        builder3.size(2);
        builder3.timeout(new TimeValue(60, TimeUnit.SECONDS));
        builder3.query(QueryBuilders.matchQuery("user", "朝阳"));

        SearchRequest searchRequest3 = new SearchRequest();
        searchRequest3.indices(INDEX_NAME);
        searchRequest3.searchType(SearchType.DFS_QUERY_THEN_FETCH);
        searchRequest3.source(builder3);
        try {
            SearchResponse searchResponse = null;
            searchResponse = client.search(searchRequest3, RequestOptions.DEFAULT);
            if (searchResponse.getHits().getTotalHits().value > 0) {
                SearchHit[] searchHit = searchResponse.getHits().getHits();
                for (SearchHit hit : searchHit) {
                    map = hit.getSourceAsMap();
                    System.out.println("map:" + Arrays.toString(map.entrySet().toArray()));

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Search 4:
        System.out.println("****************** Search 4");
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("user", "朝阳");
        MatchQueryBuilder matchQueryBuilder1 = new MatchQueryBuilder("address", "北京");

        RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder("age").from(25).to(30);
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder()
                .must(matchQueryBuilder)
                .must(matchQueryBuilder1)
                .should(rangeQueryBuilder);

        SearchSourceBuilder builder4 = new SearchSourceBuilder().query(boolQueryBuilder);
        builder4.from(0);
        builder4.size(2);
        builder4.timeout(new TimeValue(60, TimeUnit.SECONDS));
        builder4.sort("DOB", SortOrder.ASC);

        SearchRequest searchRequest4 = new SearchRequest();
        searchRequest4.indices(INDEX_NAME);
        searchRequest4.searchType(SearchType.DFS_QUERY_THEN_FETCH);
        searchRequest4.source(builder4);
        try {
            SearchResponse searchResponse = null;
            searchResponse = client.search(searchRequest4, RequestOptions.DEFAULT);
            if (searchResponse.getHits().getTotalHits().value > 0) {
                SearchHit[] searchHit = searchResponse.getHits().getHits();
                for (SearchHit hit : searchHit) {
                    map = hit.getSourceAsMap();
                    System.out.println("map:" + Arrays.toString(map.entrySet().toArray()));

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
