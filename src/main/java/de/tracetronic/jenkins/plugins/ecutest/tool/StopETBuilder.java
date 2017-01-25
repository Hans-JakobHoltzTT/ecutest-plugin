/*
 * Copyright (c) 2015-2017 TraceTronic GmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice, this
 *      list of conditions and the following disclaimer.
 *
 *   2. Redistributions in binary form must reproduce the above copyright notice, this
 *      list of conditions and the following disclaimer in the documentation and/or
 *      other materials provided with the distribution.
 *
 *   3. Neither the name of TraceTronic GmbH nor the names of its
 *      contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.tracetronic.jenkins.plugins.ecutest.tool;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.TaskListener;
import hudson.model.Run;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.apache.commons.lang.StringUtils;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

import de.tracetronic.jenkins.plugins.ecutest.ETPluginException;
import de.tracetronic.jenkins.plugins.ecutest.tool.client.ETClient;
import de.tracetronic.jenkins.plugins.ecutest.tool.installation.ETInstallation;
import de.tracetronic.jenkins.plugins.ecutest.util.EnvUtil;

/**
 * Builder providing to stop ECU-TEST.
 *
 * @author Christian Pönisch <christian.poenisch@tracetronic.de>
 */
public class StopETBuilder extends AbstractToolBuilder {

    /**
     * Defines the default timeout to stop ECU-TEST.
     */
    public static final int DEFAULT_TIMEOUT = 30;

    /**
     * Instantiates a new {@link StopETBuilder}.
     *
     * @param toolName
     *            the tool name identifying the {@link ETInstallation} to be used
     */
    @DataBoundConstructor
    public StopETBuilder(@Nonnull final String toolName) {
        super(toolName);
    }

    /**
     * Instantiates a new {@link StopETBuilder}.
     *
     * @param toolName
     *            the tool name identifying the {@link ETInstallation} to be used
     * @param timeout
     *            the timeout
     * @deprecated since 1.11 use {@link #StopETBuilder(String)}
     */
    @Deprecated
    public StopETBuilder(final String toolName, final String timeout) {
        super(toolName, StringUtils.defaultIfEmpty(timeout, String.valueOf(DEFAULT_TIMEOUT)));
    }

    /**
     * @return the default timeout
     */
    @Override
    public int getDefaultTimeout() {
        return DEFAULT_TIMEOUT;
    }

    @Override
    public void performTool(final Run<?, ?> run, final FilePath workspace, final Launcher launcher,
            final TaskListener listener) throws InterruptedException, IOException, ETPluginException {
        // Get selected ECU-TEST installation
        final ETInstallation installation = configureToolInstallation(workspace.toComputer(), listener,
                run.getEnvironment(listener));

        // Stop selected ECU-TEST
        final String toolName = run.getEnvironment(listener).expand(installation.getName());
        final EnvVars buildEnvVars = run.getEnvironment(listener);
        final int expTimeout = Integer.parseInt(EnvUtil.expandEnvVar(getTimeout(), buildEnvVars,
                String.valueOf(DEFAULT_TIMEOUT)));
        final ETClient etClient = new ETClient(toolName, expTimeout);
        if (!etClient.stop(true, workspace, launcher, listener)) {
            throw new ETPluginException(String.format("Stopping %s failed.", toolName));
        }
    }

    /**
     * DescriptorImpl for {@link StopETBuilder}.
     */
    @Symbol("stopET")
    @Extension(ordinal = 1006)
    public static final class DescriptorImpl extends AbstractToolDescriptor {

        @Override
        public int getDefaultTimeout() {
            return DEFAULT_TIMEOUT;
        }

        @Override
        public String getDisplayName() {
            return Messages.StopETBuilder_DisplayName();
        }
    }
}
