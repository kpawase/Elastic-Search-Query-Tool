package com.querytool;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = { "com.querytool" })
@EnableAsync
@EnableConfigurationProperties
public class DemoApplication {
	
	
	@Value("${elasticsearch.host}")
	private String host;

	@Value("${elasticsearch.port}")
	private int port;


	@Bean
	public RestHighLevelClient restHighLevelClient() {
		
		RestClientBuilder builder = RestClient
				.builder(new HttpHost( System.getenv("elasticsearch.host")==null ? host : System.getenv("elasticsearch.host") , 9200));

		RestHighLevelClient client = new RestHighLevelClient(builder);

		return client;

	}
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
