/*
 * Copyright 2018-2019 Bloomreach B.V. (http://www.bloomreach.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *         http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onehippo.forge.templating.support.handlebars.servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jknack.handlebars.io.TemplateLoader;
import com.github.jknack.handlebars.io.TemplateSource;

/**
 * Protocol based delegating {@link TemplateLoader} implementation.
 * For example, if a given template path is "webfile:/a/b/c.hbs", then "webfile:" is regardes as its protocol.
 */
public class ProtocolBasedDelegatingTemplateLoader implements TemplateLoader {

    private static Logger log = LoggerFactory.getLogger(ProtocolBasedDelegatingTemplateLoader.class);

    /**
     * Protocols array, which must be paired with {@link #templateLoaders} with the same size and in the same order.
     */
    private final String[] protocols;

    /**
     * Delegating {@link TemplateLoader} instances array, which must be paired with {@link #protocols} with the
     * same size and in the same order.
     */
    private final TemplateLoader[] templateLoaders;

    /**
     * Construct a delegating {@link TemplateLoader} based on the given map with pairs of protocol and corresponding
     * delegating {@link TemplateLoader} instance
     * @param protocolTemplateLoadersMap map with pairs of protocol and corresponding delegating {@link TemplateLoader}
     * instance.
     */
    public ProtocolBasedDelegatingTemplateLoader(final Map<String, TemplateLoader> protocolTemplateLoadersMap) {
        if (protocolTemplateLoadersMap == null) {
            throw new IllegalArgumentException("protocolTemplateLoadersMap shouldn't be null.");
        }

        protocols = new String[protocolTemplateLoadersMap.size()];
        templateLoaders = new TemplateLoader[protocolTemplateLoadersMap.size()];

        int index = 0;

        for (Map.Entry<String, TemplateLoader> entry : protocolTemplateLoadersMap.entrySet()) {
            protocols[index] = entry.getKey();
            templateLoaders[index] = entry.getValue();
            ++index;
        }
    }

    @Override
    public TemplateSource sourceAt(String location) throws IOException {
        for (int i = 0; i < protocols.length; i++) {
            if (location.startsWith(protocols[i])) {
                try {
                    final String locationAfterProtocol = location.substring(protocols[i].length());
                    return templateLoaders[i].sourceAt(locationAfterProtocol);
                } catch (IOException ex) {
                    // try next loader in the chain.
                    log.trace("Unable to resolve: {}, trying next loader in the chain.", location);
                }
            }
        }

        throw new FileNotFoundException(location);
    }

    @Override
    public String resolve(final String location) {
        for (int i = 0; i < protocols.length; i++) {
            if (location.startsWith(protocols[i])) {
                try {
                    final String locationAfterProtocol = location.substring(protocols[i].length());
                    templateLoaders[i].sourceAt(locationAfterProtocol);
                    return templateLoaders[i].resolve(locationAfterProtocol);
                } catch (IOException ex) {
                    // try next loader in the chain.
                    log.trace("Unable to resolve: {}, trying next loader in the chain.", location);
                }
            }
        }

        throw new IllegalStateException("Can't resolve: '" + location + "'");
    }

    @Override
    public String getPrefix() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSuffix() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPrefix(final String prefix) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSuffix(final String suffix) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCharset(final Charset charset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Charset getCharset() {
        throw new UnsupportedOperationException();
    }
}
