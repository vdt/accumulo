/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.accumulo.core.client.mapreduce;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.accumulo.core.client.IteratorSetting;
import org.apache.accumulo.core.client.Scanner;
import org.apache.accumulo.core.client.mapreduce.lib.util.InputConfigurator;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.util.format.DefaultFormatter;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

/**
 * This class allows MapReduce jobs to use multiple Accumulo tables as the source of data. This {@link org.apache.hadoop.mapreduce.InputFormat} provides keys
 * and values of type {@link Key} and {@link Value} to the Map function.
 * 
 * The user must specify the following via static configurator methods:
 * 
 * <ul>
 * <li>{@link AccumuloMultiTableInputFormat#setConnectorInfo(Job, String, org.apache.accumulo.core.client.security.tokens.AuthenticationToken)}
 * <li>{@link AccumuloMultiTableInputFormat#setScanAuthorizations(Job, org.apache.accumulo.core.security.Authorizations)}
 * <li>{@link AccumuloMultiTableInputFormat#setZooKeeperInstance(Job, String, String)} OR {@link AccumuloInputFormat#setMockInstance(Job, String)}
 * <li>{@link AccumuloMultiTableInputFormat#setBatchScanConfigs(org.apache.hadoop.mapreduce.Job, Map<String,BatchScanConfig>)}
 * </ul>
 * 
 * Other static methods are optional.
 */
public class AccumuloMultiTableInputFormat extends AbstractInputFormat<Key,Value> {

  /**
   * Sets the {@link BatchScanConfig} objects on the given Hadoop configuration
   * 
   * @param job
   *          the Hadoop job instance to be configured
   * @param configs
   *          the table query configs to be set on the configuration.
   * @since 1.6.0
   */
  public static void setBatchScanConfigs(Job job, Map<String,BatchScanConfig> configs) {
    checkNotNull(configs);
    InputConfigurator.setBatchScanConfigs(CLASS, getConfiguration(job), configs);
  }

  @Override
  public RecordReader<Key,Value> createRecordReader(InputSplit inputSplit, TaskAttemptContext context) throws IOException, InterruptedException {
    log.setLevel(getLogLevel(context));
    return new AbstractRecordReader<Key,Value>() {
      @Override
      public boolean nextKeyValue() throws IOException, InterruptedException {
        if (scannerIterator.hasNext()) {
          ++numKeysRead;
          Map.Entry<Key,Value> entry = scannerIterator.next();
          currentK = currentKey = entry.getKey();
          currentV = currentValue = entry.getValue();
          if (log.isTraceEnabled())
            log.trace("Processing key/value pair: " + DefaultFormatter.formatEntry(entry, true));
          return true;
        }
        return false;
      }

      @Override
      protected void setupIterators(TaskAttemptContext context, Scanner scanner, String tableName) {
        List<IteratorSetting> iterators = getBatchScanConfig(context, tableName).getIterators();
        for (IteratorSetting setting : iterators) {
          scanner.addScanIterator(setting);
        }
      }
    };
  }
}
