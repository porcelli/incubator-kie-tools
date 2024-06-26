/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import { BaseSymbol } from "./BaseSymbol";
import { Type } from "./Type";

import { FeelSyntacticSymbolNature } from "../FeelSyntacticSymbolNature";
import { Variable } from "../Variable";
import { Scope } from "./Scope";

export class VariableSymbol extends BaseSymbol {
  private readonly _symbolType: FeelSyntacticSymbolNature | undefined;
  private readonly _variableSource: Variable | undefined;
  private readonly _allowDynamicVariables: boolean | undefined;

  constructor(
    id?: string,
    type?: Type,
    variableType?: FeelSyntacticSymbolNature,
    variableSource?: Variable,
    allowDynamicVariables?: boolean
  ) {
    super(id, type);
    this._symbolType = variableType;
    this._variableSource = variableSource;
    this._allowDynamicVariables = allowDynamicVariables;
  }

  get symbolType(): FeelSyntacticSymbolNature | undefined {
    return this._symbolType;
  }

  get variableSource(): Variable | undefined {
    return this._variableSource;
  }

  get allowDynamicVariables(): boolean | undefined {
    return this._allowDynamicVariables;
  }
}
