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

import org.apache.nlpcraft.NCResultType.*

object NCResult:
    def apply(): NCResult = new NCResult()
    def apply(body: Any, resultType: NCResultType): NCResult = new NCResult(body = body, resultType = resultType)
    def apply(body: Any, resultType: NCResultType, intentId: String): NCResult = new NCResult(body = body, resultType =resultType, intentId)

class NCResult(var body: Any = null, var resultType: NCResultType = null, var intentId: String = null)
