#  Licensed to the Apache Software Foundation (ASF) under one or more
#  contributor license agreements.  See the NOTICE file distributed with
#  this work for additional information regarding copyright ownership.
#  The ASF licenses this file to You under the Apache License, Version 2.0
#  (the "License"); you may not use this file except in compliance with
#  the License.  You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#

import logging
from flask import Flask
from flask import request
from bertft import Pipeline

logging.basicConfig(format='%(asctime)s - %(name)s - %(levelname)s - %(message)s', level=logging.DEBUG)

app = Flask(__name__)

pipeline = Pipeline()


class ValidationException(Exception):
    def __init__(self, message):
        super().__init__(message)


@app.errorhandler(ValidationException)
def handle_bad_request(e):
    return str(e), 400


def check_condition(condition, supplier, message):
    if condition:
        return supplier()
    else:
        raise ValidationException(message)


def present(json, name):
    return check_condition(name in json, lambda: json[name],
                           "Required '" + name + "' argument is not present")


@app.route('/synonyms', methods=['POST'])
def main():
    if not request.is_json:
        raise ValidationException("Json expected")

    json = request.json

    sentence = present(json, 'sentence')
    upper = present(json, 'upper')
    lower = present(json, 'lower')
    positions = check_condition(lower <= upper, lambda: [lower, upper],
                                "Lower bound must be less or equal upper bound")
    limit = present(json, 'limit')

    data = pipeline.do_find(sentence, positions, limit)
    if 'simple' not in json or not json['simple']:
        json_data = data.to_json(orient='table', index=False)
    else:
        json_data = data['word'].to_json(orient='values')
    return app.response_class(response=json_data, status=200, mimetype='application/json')
