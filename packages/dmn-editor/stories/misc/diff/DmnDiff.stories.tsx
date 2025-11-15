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

import * as React from "react";
import type { Meta, StoryObj } from "@storybook/react";
import { DmnDiffUploader } from "../../../src/diff/components/DmnDiffUploader";
import { computeDmnDiff } from "../../../src/diff/algorithms/dmnDiffAlgorithm";
import { useDmnDiffStore } from "../../../src/diff/store/DmnDiffStore";
import "@patternfly/react-core/dist/styles/base.css";

const meta: Meta<typeof DmnDiffUploader> = {
  title: "Misc/DMN Diff",
  component: DmnDiffUploader,
  parameters: {
    layout: "fullscreen",
  },
};

export default meta;
type Story = StoryObj<typeof DmnDiffUploader>;

export const DmnDiff: Story = {
  render: () => <DmnDiffUploader />,
};

const DmnDiffAlgoPlayground: React.FC = () => {
  const versionA = useDmnDiffStore((state) => state.versionA);
  const versionB = useDmnDiffStore((state) => state.versionB);
  const isReadyForComparison = useDmnDiffStore((state) => state.isReadyForComparison());
  const lastComparedRef = React.useRef<{ versionA: typeof versionA; versionB: typeof versionB } | null>(null);

  React.useEffect(() => {
    if (!isReadyForComparison || !versionA?.model || !versionB?.model) {
      return;
    }

    if (lastComparedRef.current?.versionA === versionA && lastComparedRef.current?.versionB === versionB) {
      return;
    }

    lastComparedRef.current = { versionA, versionB };

    try {
      const diff = computeDmnDiff(versionA.model, versionB.model);
      const successMessage = `DMN diff complete.\nNode changes: ${diff.nodes.length}\nEdge changes: ${diff.edges.length}`;
      if (typeof globalThis.alert === "function") {
        globalThis.alert(successMessage);
      } else {
        console.log(successMessage);
      }
    } catch (error) {
      const failureMessage = `Failed to compute diff: ${error instanceof Error ? error.message : "Unknown error"}`;
      if (typeof globalThis.alert === "function") {
        globalThis.alert(failureMessage);
      } else {
        console.error(failureMessage);
      }
    }
  }, [isReadyForComparison, versionA, versionB]);

  return <DmnDiffUploader />;
};

export const DiffAlgo: Story = {
  name: "Diff Algorithm Test",
  render: () => <DmnDiffAlgoPlayground />,
};
