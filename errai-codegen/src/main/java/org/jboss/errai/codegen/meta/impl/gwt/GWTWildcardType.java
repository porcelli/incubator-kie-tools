/*
 * Copyright 2011 JBoss, by Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.errai.codegen.meta.impl.gwt;

import org.jboss.errai.codegen.meta.MetaType;
import org.jboss.errai.codegen.meta.MetaWildcardType;

import com.google.gwt.core.ext.typeinfo.JWildcardType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
public class GWTWildcardType implements MetaWildcardType {
  private final JWildcardType wildcardType;
  private final TypeOracle oracle;

  public GWTWildcardType(TypeOracle oracle, JWildcardType wildcardType) {
    this.wildcardType = wildcardType;
    this.oracle = oracle;
  }

  @Override
  public MetaType[] getLowerBounds() {
    return GWTUtil.fromTypeArray(oracle, wildcardType.getLowerBounds());
  }

  @Override
  public MetaType[] getUpperBounds() {
    return GWTUtil.fromTypeArray(oracle, wildcardType.getUpperBounds());
  }

  @Override
  public String toString() {
    return getName();
  }

  @Override
  public String getName() {
    return wildcardType.getParameterizedQualifiedSourceName();
  }
}
