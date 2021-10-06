/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.nlpcraft.model;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The corpus of intent samples that is used for documentaiton and model auto-validation.
 * <p>
 * This annotation allows to load these samples from the external sources like local file or URL and
 * should be used together with {@link NCIntent} or {@link NCIntentRef} annotations on the callback
 * methods. Method can have multiple annotations of this type and each annotation can define multiple input
 * examples. See similar {@link NCIntentSample} annotation that allows to define samples in place.
 * <p>
 * Here's an example of using this annotation:
 * <pre class="brush: java, highlight: [2]">
 * {@literal @}NCIntentRef("alarm")
 * {@literal @}NCIntentSampleRef("alarm_samples.txt")
 * NCResult onMatch(
 *      NCIntentMatch ctx,
 *      {@literal @}NCIntentTerm("nums") List&lt;NCToken&gt; numToks
 * ) {
 *     ...
 * }
 * </pre>
 * <p>
 * Read full documentation in <a target=_ href="https://nlpcraft.apache.org/intent-matching.html#binding">Intent Matching</a> section and review
 * <a target=_ href="https://github.com/apache/incubator-nlpcraft/tree/master/nlpcraft-examples">examples</a>.
 *
 * @see NCIntentSample
 * @see NCIntent
 * @see NCIntentRef
 * @see NCIntentTerm
 * @see NCModelAddClasses
 * @see NCModelAddPackage
 * @see NCIntentSkip
 * @see NCIntentMatch
 * @see NCModel#onMatchedIntent(NCIntentMatch)
 * @see NCTestAutoModelValidator
 */
@Retention(value=RUNTIME)
@Target(value=METHOD)
@Repeatable(NCIntentSampleRef.NCIntentSampleList.class)
public @interface NCIntentSampleRef {
    /**
     * Local file path, classpath resource path or URL supported by {@link java.net.URL} class. The content of the source
     * should be a new-line separated list of string. Empty strings and strings starting with '#" (hash) symbol will
     * be ignored. This annotation should be attached the intent callback method. Note that using this annotation is equivalent
     * to using {@link NCIntentSample} annotation and listing all of its samples in place instead of an external source.
     *
     * @return Local file path, classpath resource path or URL supported by {@link java.net.URL} class.
     */
    String value();

    /**
     * Grouping annotation required for when more than one {@link NCIntentSampleRef} annotation is used.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(value=METHOD)
    @Documented
    @interface NCIntentSampleList {
        /**
         * Gets the list of all {@link NCIntentSampleRef} annotations attached to the callback.
         *
         * @return List of all {@link NCIntentSampleRef} annotations attached to the callback.
         */
        NCIntentSampleRef[] value();
    }
}
