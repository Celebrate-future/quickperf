/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2019-2019 the original author or authors.
 */

package org.quickperf.sql.select;

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.proxy.ParameterSetOperation;
import org.junit.Test;
import org.quickperf.measure.BooleanMeasure;
import org.quickperf.sql.SqlExecutions;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HasExactlySameSelectExtractorTest {

    //Not a use case with requests executed on a database waiting for to have a clear use case
    @Test public void
    should_return_true_with_exactly_same_selects_without_params() throws SQLException {

        // GIVEN
        String select = "SELECT * FROM Book";

        QueryInfo queryInfo1 = mock(QueryInfo.class);
        when(queryInfo1.getQuery()).thenReturn(select);
        List<List<ParameterSetOperation>> noParameterList = emptyList();
        when(queryInfo1.getParametersList()).thenReturn(noParameterList);

        QueryInfo queryInfo2 = mock(QueryInfo.class);
        when(queryInfo2.getQuery()).thenReturn(select);
        when(queryInfo2.getParametersList()).thenReturn(noParameterList);

        ExecutionInfo execInfo = mock(ExecutionInfo.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(execInfo.getResult()).thenReturn(resultSet);
        when(resultSet.getMetaData()).thenReturn(mock(ResultSetMetaData.class));

        SqlExecutions sqlExecutions = SqlExecutions.NONE;
        sqlExecutions.add(execInfo
                       , asList(queryInfo1, queryInfo2));

        HasExactlySameSelectExtractor extractor = HasExactlySameSelectExtractor.INSTANCE;

        // WHEN
        BooleanMeasure sameSelectsWithDifferentParams = extractor.extractPerfMeasureFrom(sqlExecutions);

        // THEN
        assertThat(sameSelectsWithDifferentParams.getValue()).isTrue();

    }

}