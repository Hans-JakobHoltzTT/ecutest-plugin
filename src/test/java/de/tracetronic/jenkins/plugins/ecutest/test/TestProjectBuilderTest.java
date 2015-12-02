/**
 * Copyright (c) 2015 TraceTronic GmbH
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
package de.tracetronic.jenkins.plugins.ecutest.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.tracetronic.jenkins.plugins.ecutest.test.config.ExecutionConfig;
import de.tracetronic.jenkins.plugins.ecutest.test.config.ProjectConfig;
import de.tracetronic.jenkins.plugins.ecutest.test.config.ProjectConfig.JobExecutionMode;
import de.tracetronic.jenkins.plugins.ecutest.test.config.TestConfig;

/**
 * Unit tests for {@link TestProjectBuilderTest}.
 *
 * @author Christian Pönisch <christian.poenisch@tracetronic.de>
 */
public class TestProjectBuilderTest {

    @Test
    public void testBlankConfigShouldReturnDefaults() {
        final TestConfig testConfig = new TestConfig("", "");
        final ProjectConfig projectConfig = new ProjectConfig(false, "", JobExecutionMode.SEQUENTIAL_EXECUTION);
        final ExecutionConfig executionConfig = new ExecutionConfig("", true, true);
        final TestProjectBuilder builder = new TestProjectBuilder("", testConfig, projectConfig, executionConfig);
        assertEquals("Check default timeout", ExecutionConfig.getDefaultTimeout(), builder.getExecutionConfig()
                .getTimeout());
    }

    @Test
    public void testNull() {
        final TestConfig testConfig = new TestConfig(null, null, null);
        final ProjectConfig projectConfig = new ProjectConfig(false, null, JobExecutionMode.SEQUENTIAL_EXECUTION);
        final ExecutionConfig executionConfig = new ExecutionConfig(null, false, false);
        final TestProjectBuilder builder = new TestProjectBuilder("", testConfig, projectConfig, executionConfig);
        assertNotNull(builder);
        assertNotNull(builder.getTestFile());
        assertTrue(builder.getTestFile().isEmpty());
        assertNotNull(builder.getTestConfig().getTbcFile());
        assertTrue(builder.getTestConfig().getTbcFile().isEmpty());
        assertNotNull(builder.getTestConfig().getTcfFile());
        assertTrue(builder.getTestConfig().getTcfFile().isEmpty());
        assertTrue(builder.getTestConfig().getConstants().isEmpty());
        assertFalse(builder.getProjectConfig().isExecInCurrentPkgDir());
        assertNotNull(builder.getProjectConfig().getFilterExpression());
        assertTrue(builder.getProjectConfig().getFilterExpression().isEmpty());
        assertEquals(JobExecutionMode.SEQUENTIAL_EXECUTION, builder.getProjectConfig().getJobExecMode());
        assertNotNull(builder.getExecutionConfig().getTimeout());
        assertEquals("Check default timeout", ExecutionConfig.getDefaultTimeout(), builder.getExecutionConfig()
                .getTimeout());
        assertFalse(builder.getExecutionConfig().isStopOnError());
        assertFalse(builder.getExecutionConfig().isCheckTestFile());
    }
}
