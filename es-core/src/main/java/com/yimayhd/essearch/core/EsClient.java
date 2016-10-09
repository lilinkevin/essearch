package com.yimayhd.essearch.core;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * EsClient
 *
 * @author lilin
 * @date 16/9/27
 */

@Component
public class EsClient {

    @Value("${cluster.name}")
    private String clusterName;

    private Client client;

    public Client getClient() {
        return client;
    }

    @PostConstruct
    public void init(){
        client = getTransportClient();
    }

    public Client getTransportClient(){
        Settings settings = Settings.settingsBuilder()
                .put("cluster.name", clusterName)
                .put("client.transport.sniff",true)
                .build();
        try {
            return TransportClient.builder().settings(settings).build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.0.165"),9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }
}
