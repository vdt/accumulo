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
/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.apache.accumulo.proxy.thrift;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

@SuppressWarnings("all") public enum PTablePermission implements org.apache.thrift.TEnum {
  READ(2),
  WRITE(3),
  BULK_IMPORT(4),
  ALTER_TABLE(5),
  GRANT(6),
  DROP_TABLE(7);

  private final int value;

  private PTablePermission(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static PTablePermission findByValue(int value) { 
    switch (value) {
      case 2:
        return READ;
      case 3:
        return WRITE;
      case 4:
        return BULK_IMPORT;
      case 5:
        return ALTER_TABLE;
      case 6:
        return GRANT;
      case 7:
        return DROP_TABLE;
      default:
        return null;
    }
  }
}
