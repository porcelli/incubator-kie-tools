/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

export function buildXmlHref({ namespace, id }: { namespace?: string; id: string }) {
  return `${namespace ?? ""}#${id}`;
}

export type XmlHref = {
  namespace: string | undefined;
  id: string;
};

export function parseXmlHref(href: string): XmlHref {
  const split = href.split("#");

  if (split.length <= 1) {
    return { namespace: undefined, id: split[0] };
  }

  if (split.length > 2) {
    throw new Error(`XML URI can't have hashes (#) on neither the namespace or the id. Alledged URI: '${href}'`);
  }

  return { namespace: split[0] ? split[0] : undefined, id: split[1] };
}

/**
 * This function adds a `namespace` to an HREF. This operation will only succed if
 * the provided HREF doesn't have a `namespace`.
 *
 * In case the provided `namespace` is `undefined`, this function will return the HREF.
 *
 * In case the provided HREF already have an `namespace`, this function will return the HREF.
 */
export function addNamespaceToHref({ href, namespace }: { href: string; namespace: string | undefined }) {
  if (namespace === undefined) {
    return href;
  }

  const { namespace: hrefNamespace, id } = parseXmlHref(href);
  if (hrefNamespace !== undefined) {
    return href;
  }

  return buildXmlHref({ namespace, id });
}
