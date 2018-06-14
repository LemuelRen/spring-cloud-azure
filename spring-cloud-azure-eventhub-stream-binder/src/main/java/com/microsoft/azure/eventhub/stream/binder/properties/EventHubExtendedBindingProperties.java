/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

package com.microsoft.azure.eventhub.stream.binder.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.stream.binder.ExtendedBindingProperties;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Warren Zhu
 */
@ConfigurationProperties("spring.cloud.stream.eventhub")
public class EventHubExtendedBindingProperties
        implements ExtendedBindingProperties<EventHubConsumerProperties, EventHubProducerProperties> {

    private Map<String, EventHubBindingProperties> bindings = new ConcurrentHashMap<>();
    private String namespace;
    private String checkpointStorageAccount;
    private String checkpointStorageAccountContainer;

    public String getCheckpointStorageAccount() {
        return checkpointStorageAccount;
    }

    public void setCheckpointStorageAccount(String checkpointStorageAccount) {
        this.checkpointStorageAccount = checkpointStorageAccount;
    }

    public String getCheckpointStorageAccountContainer() {
        return checkpointStorageAccountContainer;
    }

    public void setCheckpointStorageAccountContainer(String checkpointStorageAccountContainer) {
        this.checkpointStorageAccountContainer = checkpointStorageAccountContainer;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public EventHubConsumerProperties getExtendedConsumerProperties(String channelName) {
        return this.bindings.computeIfAbsent(channelName, key -> new EventHubBindingProperties()).getConsumer();
    }

    @Override
    public EventHubProducerProperties getExtendedProducerProperties(String channelName) {
        return this.bindings.computeIfAbsent(channelName, key -> new EventHubBindingProperties()).getProducer();
    }
}