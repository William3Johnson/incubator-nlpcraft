/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.nlpcraft

/**
  * A tokenizer that splits given text into the list of [[NCToken]] objects. This is one of the user-defined
  * components of the processing [[NCPipeline pipeline]]. See [[NCPipeline]] for documentation on the token
  * parser place in the overall processing pipeline.
  *
  * @see [[NCEntity]]
  * @see [[NCToken]]
  * @see [[NCTokenParser]]
  * @see [[NCTokenEnricher]]
  * @see [[NCTokenValidator]]
  * @see [[NCEntityParser]]
  * @see [[NCEntityEnricher]]
  * @see [[NCEntityValidator]]
  * @see [[NCEntityMapper]]
  */
trait NCTokenParser:
    /**
      * Splits given text into list of [[NCToken]] objects.
      *
      * @param text Input text to split.
      * @return List of split token. Can be empty but should never be `null`.
      */
    def tokenize(text: String): List[NCToken]
