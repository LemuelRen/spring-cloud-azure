/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

package com.microsoft.azure.spring.cloud.keyvault.config;

import com.microsoft.azure.Page;
import com.microsoft.azure.PagedList;
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.models.PageImpl;
import com.microsoft.azure.keyvault.models.SecretItem;
import com.microsoft.azure.spring.cloud.keyvault.config.auth.Credentials;
import com.microsoft.rest.RestException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KeyVaultPropertySourceLocatorTest {
    private static final String CLIENT_ID = "fake-client-id";
    private static final String CLIENT_SECRET = "fake-client-secret";
    private static final String DEFAULT_PROFILE_0 = "default-profile-0";
    private static final String DEFAULT_PROFILE_1 = "default-profile-1";
    private static final String DEFAULT_PROFILE_2 = "default-profile-2";

    private static final KeyVaultClient client = mock(KeyVaultClient.class);
    private static final ConfigurableEnvironment environment = mock(ConfigurableEnvironment.class);
    private static final Credentials credentials = new Credentials();

    @BeforeClass
    public static void setup() {
        credentials.setClientId(CLIENT_ID);
        credentials.setClientSecret(CLIENT_SECRET);

        when(environment.getActiveProfiles())
                .thenReturn(new String[]{DEFAULT_PROFILE_0, DEFAULT_PROFILE_1, DEFAULT_PROFILE_2});

        when(client.listSecrets(anyString()))
                .thenReturn(new PagedList<SecretItem>(new PageImpl<>()) {
                    @Override
                    public Page<SecretItem> nextPage(String s) throws RestException {
                        return null;
                    }
                });
    }

    @Test
    public void testDefaultActiveProfiles() {
        KeyVaultConfigProperties properties = new KeyVaultConfigProperties();
        properties.setCredentials(credentials);

        KeyVaultPropertySourceLocator locator = new KeyVaultPropertySourceLocator(client, properties);
        PropertySource<?> propertySource = locator.locate(environment);

        assertThat(propertySource).isInstanceOf(CompositePropertySource.class);

        Collection<PropertySource<?>> propertySources = ((CompositePropertySource)propertySource).getPropertySources();
        assertThat(propertySources.size()).isEqualTo(3);
        
    }

    @Test
    public void testCustomActiveProfiles() {

    }
}
