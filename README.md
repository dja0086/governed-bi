# governed-bi

Governed, AI-native, real-time BI for NetSuite-centric enterprises.

One typed manifest per dataset is the definition. Compilers generate the pipeline, the semantic model, the tests and the dashboards from it. Cube is the only serving surface. Certified answers appear only in server-rendered components.

## Status

Phase 0 (evidence gate). See `STATUS.md`.

## Where to start

| You are | Read first |
|---|---|
| The owner | `GOVERNANCE.md`, `STATUS.md`, `DECISIONS.md` |
| A new engineer or finance owner | `HANDOFF.md` |
| An AI coding agent | `CLAUDE.md` |

## Layout

| Path | Holds |
|---|---|
| `spec/` | Manifest schema, metric types, filter grammar, adapter contract |
| `manifests/` | One manifest per dataset (the definitions) |
| `config/` | Core pack and per-client overrides |
| `phase0/` | Spikes, bake-off scoring, gate pack |
| `tools/` | Validation and, later, compilers |
| `docs/` | Plans and council reviews this project is based on |
| `.github/` | CI, code owners, pull request template |

## Rules that do not bend

1. Nobody edits generated output. Change the manifest.
2. Only the owner certifies a metric or changes a certified number.
3. No writes to NetSuite without the owner.
4. No SQL in client overrides.
5. Unverified claims are labelled unverified until a spike or test proves them.
